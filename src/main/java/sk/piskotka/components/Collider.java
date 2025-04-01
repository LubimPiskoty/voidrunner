package sk.piskotka.components;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.piskotka.logger.Logger;
import sk.piskotka.physics.CollisionEvents;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Drawable;
import sk.piskotka.render.Renderer;

//TODO: Check if shape is convex and split up into multiple colliders if needed
public class Collider extends Component implements Drawable {
    private List<Vec2> vertices;
    private Transform transform;
    
    private double boundingCircleRadius;
    private Vec2 boundingCircleCenter;

    private Set<Collider> colliders;
    private CollisionEvents collisionEvents;

    public class CollisionInfo {
        public boolean result;
        public Vec2 normal;
        public double penetration;
    }

    public Collider(PhysicsBody pBody, List<Vec2> vertices){
        super(pBody);
        if (vertices == null || vertices.size() < 3)
            Logger.throwError(getClass(), "Vertices are null or smaller then 3. Try checking object instantiation");
        
        if (pBody == null)
            Logger.throwError(getClass(), "PhysicalBody is null. Try checking object instantiation");
        
        this.transform = getPhysicsBody();
        this.vertices = vertices;
        this.colliders = new HashSet<>();
        this.collisionEvents = pBody;
        computeBoundingCircle();
    }

    // private List<Vec2> computeEdgeNormals(List<Vec2> vertices){
    //     List<Vec2> normals = new ArrayList<>();

    //     Vec2 prev = vertices.get(vertices.size()-1);
    //     for(Vec2 next : vertices){
    //         normals.add(prev.subtract(next).normal().normalized());
    //         prev = next;
    //     }

    //     return normals;
    // }

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

    // First check the bounding collision and then construct SAT
    public CollisionInfo checkCollisionWith(Collider other){
        CollisionInfo cInfo = new CollisionInfo();
        if (!isEnabled) // Do not check for collision if the component is not enabled
            return cInfo;

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
        // Shape circleShape = new PolygonShape(boundingCircleCenter.getX(), boundingCircleCenter.getY(),
        //                                     (int)boundingCircleRadius, 16);
        // Vec2 pos = transform.getGlobalPos();
        // ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), circleShape.getPoints(), isColliding() ? Color.RED.darker() : Color.GREEN.darker());

    }

    @Override
    public String toString() {
        return transform.toString()+"-Collider";
    }

    private void removeCollider(Collider other){
        colliders.remove(other);
    }

    public void onDestroy() {
        Destroy();
        for(Collider collider : colliders){
            collider.removeCollider(this);
        }
    }
}