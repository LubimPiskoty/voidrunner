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

/**
 * The Collider class represents a physical collider component that can detect collisions
 * with other colliders. It uses bounding circles for preliminary collision checks and 
 * supports collision events.
 */
public class Collider extends Component implements Drawable {
    private List<Vec2> vertices;
    private final Transform transform;
    
    private double boundingCircleRadius;
    private Vec2 boundingCircleCenter;

    private final Set<Collider> colliders;
    private CollisionEvents collisionEvents;

    /**
     * Stores information about a collision, including the result, normal vector, and penetration depth.
     */
    public class CollisionInfo {
        public boolean result;
        public Vec2 normal;
        public double penetration;
    }

    /**
     * Constructs a Collider with the specified PhysicsBody and vertices.
     *
     * @param pBody    The PhysicsBody associated with this collider.
     * @param vertices The vertices defining the shape of the collider. Must have at least 3 vertices.
     * @throws IllegalArgumentException if vertices are null or have fewer than 3 points, or if pBody is null.
     */
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

    /**
     * Computes the bounding circle for the collider based on its vertices.
     * The bounding circle is used for preliminary collision detection.
     */
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

    /**
     * Checks if the bounding circles of this collider and another collider are colliding.
     *
     * @param other The other collider to check against.
     * @return True if the bounding circles are colliding, false otherwise.
     */
    private boolean isBoundingColliding(Collider other){
        return boundingCircleCenter.subtract(other.boundingCircleCenter)
                .add(transform.getGlobalPos()).subtract(other.transform.getGlobalPos()).length() 
                <= boundingCircleRadius + other.boundingCircleRadius;
    }

    /**
     * Checks for a collision with another collider. If a collision is detected,
     * collision events are triggered, and the colliders are added to each other's
     * collision sets.
     *
     * @param other The other collider to check for collision.
     * @return A CollisionInfo object containing the collision result and details.
     */
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

    /**
     * Determines if this collider is currently colliding with any other collider.
     *
     * @return True if this collider is colliding, false otherwise.
     */
    public boolean isColliding(){
        return !colliders.isEmpty();
    }


    /**
     * Draws the collider for debugging purposes. This method is overridden from the Drawable interface.
     *
     * @param ctx The Renderer context used for drawing.
     */
    @Override
    public void draw(Renderer ctx) {
        // Debug only
        // Shape circleShape = new PolygonShape(boundingCircleCenter.getX(), boundingCircleCenter.getY(),
        //                                     (int)boundingCircleRadius, 16);
        // Vec2 pos = transform.getGlobalPos();
        // ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), circleShape.getPoints(), isColliding() ? Color.RED.darker() : Color.GREEN.darker());

    }

    /**
     * Returns a string representation of the collider, including its transform information.
     *
     * @return A string representation of the collider.
     */
    @Override
    public String toString() {
        return transform.toString()+"-Collider";
    }

    /**
     * Removes a collider from this collider's collision set.
     *
     * @param other The collider to remove.
     */
    private void removeCollider(Collider other){
        colliders.remove(other);
    }

    /**
     * Cleans up the collider when it is destroyed. Removes this collider from the collision sets
     * of all other colliders it is currently colliding with.
     */
    public void onDestroy() {
        Destroy();
        for(Collider collider : colliders){
            collider.removeCollider(this);
        }
    }
}