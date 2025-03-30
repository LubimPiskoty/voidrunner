package sk.piskotka.physics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.logger.Logger;
import sk.piskotka.render.Drawable;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;
import sk.piskotka.shapes.Shape;

//TODO: Check if shape is convex and split up into multiple colliders if needed


public class Collider implements Drawable {
    private List<Vec2> vertices;
    private List<Vec2> edgeNormals;
    private Transform transform;
    
    private double boundingCircleRadius;
    private Vec2 boundingCircleCenter;

    private Set<Collider> colliders;
    private CollisionEvents collisionEvents;

    public class CollisionInfo {
        public boolean result;
        public Vec2 normal;
        public double penetration;
        public List<Vec2> contacts;
        private Collider refCollider;
        private Collider incCollider;
    }

    public Collider(PhysicsBody pBody, List<Vec2> vertices){
        if (vertices == null || vertices.size() < 3)
            Logger.throwError(getClass(), "Vertices are null or smaller then 3. Try checking object instantiation");
        
        if (pBody == null)
            Logger.throwError(getClass(), "PhysicalBody is null. Try checking object instantiation");
        
        this.transform = pBody;
        this.vertices = vertices;
        this.edgeNormals = computeEdgeNormals(vertices);
        this.colliders = new HashSet<>();
        this.collisionEvents = pBody;
        computeBoundingCircle();
    }

    private List<Vec2> computeEdgeNormals(List<Vec2> vertices){
        List<Vec2> normals = new ArrayList<>();

        Vec2 prev = vertices.get(vertices.size()-1);
        for(Vec2 next : vertices){
            normals.add(prev.subtract(next).normal().normalized());
            prev = next;
        }

        return normals;
    }

    private void computeBoundingCircle(){

        boundingCircleCenter = Vec2.ZERO();
        boundingCircleRadius = Double.POSITIVE_INFINITY;

        // Find the centroid
        for(Vec2 p : vertices)
            boundingCircleCenter.add(p);
        boundingCircleCenter.multiply(1/vertices.size());

        //Find the furthest points from centroid
        for(Vec2 p : vertices){
            double dist = boundingCircleCenter.subtract(p).length();
            if (boundingCircleRadius > dist)
                boundingCircleRadius = dist;
        }
    }

    //TODO: Refactor this monstrosity
    private boolean isBoundingColliding(Collider other){
        return boundingCircleCenter.subtract(other.boundingCircleCenter)
                .add(transform.getGlobalPos()).subtract(other.transform.getGlobalPos()).length() 
                <= boundingCircleRadius + other.boundingCircleRadius;
    }

    private static double[] projectColliderToNormal(Collider collider, Vec2 normal){
        double high = Double.NEGATIVE_INFINITY;
        double low = Double.POSITIVE_INFINITY;
        for(Vec2 vert : collider.transform.transformPoints(collider.vertices)){
            double projected = normal.dot(vert);
            high = Math.max(high, projected);
            low = Math.min(low, projected);
        }
        return new double[]{high, low};
    }

    private static boolean isCollidingFromA(Collider A, Collider B, CollisionInfo outInfo){
        outInfo.penetration = Double.POSITIVE_INFINITY;
        // Find separating line
        for(Vec2 edgeNorm : A.edgeNormals){
            double[] projA = projectColliderToNormal(A, edgeNorm);
            double[] projB = projectColliderToNormal(B, edgeNorm);

            if (projA[0] < projB[1] || projA[1] > projB[0])
                return outInfo.result = false;
            
            double penetration = Math.min(projA[0] - projB[1], projB[0] - projA[1]);
            // Logger.logDebug(Collider.class,
            //     String.format("projA: %s, projB: %s", new Vec2(projA[0], projA[1]), new Vec2(projB[0], projB[1])));
            
            if (penetration < outInfo.penetration){
                outInfo.penetration = penetration;
                outInfo.normal = edgeNorm;
                outInfo.refCollider = A;
                outInfo.incCollider = B;
            }
        }

        for(Vec2 edgeNorm : B.edgeNormals){
            double[] projA = projectColliderToNormal(A, edgeNorm);
            double[] projB = projectColliderToNormal(B, edgeNorm);

            if (projA[0] < projB[1] || projA[1] > projB[0])
                return outInfo.result = false;
                
            double penetration = Math.min(projA[0] - projB[1], projB[0] - projA[1]);
            if (penetration < outInfo.penetration){
                // Logger.logDebug(Collider.class,
                //     String.format("projA: %s, projB: %s", new Vec2(projA[0], projA[1]), new Vec2(projB[0], projB[1])));
                outInfo.penetration = penetration;
                outInfo.normal = edgeNorm;
                outInfo.refCollider = B;
                outInfo.incCollider = A;
            }
            
        }   
        return outInfo.result = true;
    }

    // First check the bounding collision and then construct SAT
    public CollisionInfo checkCollisionWith(Collider other){
        CollisionInfo cInfo = new CollisionInfo();
        if (isBoundingColliding(other)){
            collisionEvents.onCollision(other);
            other.collisionEvents.onCollision(this);
            colliders.add(other);
            other.colliders.add(this);

        }else{
            colliders.remove(other);
            other.colliders.remove(this);
        }
        return cInfo;
    }

    public boolean isColliding(){
        return colliders.size() != 0;
    }


    @Override
    public void draw(Renderer ctx) {
        // Debug only
        Shape circleShape = new PolygonShape(boundingCircleCenter.getX(), boundingCircleCenter.getY(),
                                            (int)boundingCircleRadius, 16);
        Vec2 pos = transform.getGlobalPos();
        ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), circleShape.getPoints(), isColliding() ? Color.RED.darker() : Color.GREEN.darker());

    }

    private static void debugEdgeNormals(Collider c, int edgeIndex, Color color){
        if (!GameManager.isDebug())
            return;

        Vec2 prev = c.vertices.get((edgeIndex + c.vertices.size()-1)%c.vertices.size());
        Vec2 next = c.vertices.get(edgeIndex);
        GameManager.getRenderer().drawVector(
            c.transform.getGlobalFromLocalPos(prev.add(next).multiply(0.5)),
            c.edgeNormals.get(edgeIndex).multiply(prev.subtract(next).length()/2).rotated(c.transform.getLocalRot()), color);
    
    }

    @Override
    public String toString() {
        return transform.toString()+"-Collider";
    }

    private void removeCollider(Collider other){
        colliders.remove(other);
    }

    public void onDestroy() {
        for(Collider collider : colliders){
            collider.removeCollider(this);
        }
    }
}