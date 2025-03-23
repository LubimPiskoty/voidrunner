package sk.piskotka.physics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.paint.Color;
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

    private static boolean isCollidingFromA(Collider A, Collider B){
        // Find separating line
        for(Vec2 edgeNorm : A.edgeNormals){
            double[] projA = projectColliderToNormal(A, edgeNorm);
            double[] projB = projectColliderToNormal(B, edgeNorm);

            // Logger.logDebug(Collider.class, 
            //                 String.format("ProjA: (%.2f, %.2f)\t ProjB: (%.2f, %.2f)", projA[0], projA[1], projB[0], projB[1]));
            if (projA[0] < projB[1] || projA[1] > projB[0])
                return false;
            
        }
        return true;
    }

    // First check the bounding collision and then construct SAT
    public boolean checkCollisionWith(Collider other){
        //                                                                    Why was this part in the blog if it makes it worse?
        if (isBoundingColliding(other) && isCollidingFromA(this, other)){// || isCollidingFromA(other, this));
            collisionEvents.onCollision(other);
            other.collisionEvents.onCollision(this);
            colliders.add(other);
            other.colliders.add(this);
            return true;
        }
        colliders.remove(other);
        other.colliders.remove(this);
        return false;
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

        
        // Draw all debug edge normals
        // Vec2 prev = vertices.get(vertices.size()-1);
        // Vec2 next;
        // for(int i = 0; i < edgeNormals.size(); i++, prev = next){
        //     next = vertices.get(i);
        //     Vec2 globalVert = transform.getGlobalFromLocalPos(next);
        //     ctx.drawCross(globalVert.getX(), globalVert.getY(), 5, Color.CRIMSON);
        //     ctx.drawVector(
        //         transform.getGlobalFromLocalPos(prev.add(next).multiply(0.5)),
        //         edgeNormals.get(i).multiply(prev.subtract(next).length()/2).rotated(transform.getLocalRot()), Color.INDIGO);
        // }
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