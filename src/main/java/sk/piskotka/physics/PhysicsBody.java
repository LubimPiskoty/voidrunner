package sk.piskotka.physics;

import sk.piskotka.components.Collider;
import sk.piskotka.components.Collider.CollisionInfo;
import sk.piskotka.logger.Logger;
import sk.piskotka.render.Drawable;
import sk.piskotka.shapes.Shape;

/**
 * Represents a physical body in the game world that can move, collide, and interact with other objects.
 * This class extends {@link Transform} and implements {@link Drawable} and {@link CollisionEvents}.
 */
public abstract class PhysicsBody extends Transform implements Drawable, CollisionEvents {
    public Collider collider;

    /**
     * Gets the collider associated with this physics body.
     * 
     * @return the {@link Collider} of this physics body.
     */
    public Collider getCollider() {return collider;}

    private Shape shape;

    /**
     * Gets the shape of this physics body.
     * 
     * @return the {@link Shape} of this physics body.
     */
    public Shape getShape() {return shape;}

    /**
     * Sets the shape of this physics body and updates its collider.
     * 
     * @param shape the new {@link Shape} to set.
     */
    public void setShape(Shape shape) 
    {
        this.shape = shape;
        collider = new Collider(this, shape.getPoints());
    }

    private Vec2 vel, acc;

    /**
     * Gets the velocity of this physics body.
     * 
     * @return the velocity as a {@link Vec2}.
     */
    public Vec2 getVelocity() {return vel;}

    /**
     * Sets the velocity of this physics body.
     * 
     * @param vel the new velocity as a {@link Vec2}.
     */
    public void setVelocity(Vec2 vel) {this.vel = vel;}

    protected float speed;
    protected float maxSpeed;

    /**
     * Constructs a new {@code PhysicsBody} with the specified position and rotation.
     * 
     * @param x the x-coordinate of the initial position.
     * @param y the y-coordinate of the initial position.
     * @param rotation the initial rotation in degrees.
     */
    public PhysicsBody(double x, double y, double rotation){
        super(new Vec2(x, y));
        this.setRotation(rotation);
        this.speed = 0;
        this.maxSpeed = 350;
        this.acc = Vec2.ZERO();
        this.vel = Vec2.ZERO();
    }

    /**
     * Applies a force to this physics body, modifying its acceleration.
     * 
     * @param vec the force to apply as a {@link Vec2}.
     */
    public void ApplyForce(Vec2 vec){
        acc = acc.add(vec);   
    }

    /**
     * Sets the velocity of this physics body and ensures it does not exceed the maximum speed.
     * 
     * @param vel the new velocity as a {@link Vec2}.
     */
    public void SetVelocity(Vec2 vel){
        this.vel.set(vel);
        limitVelocity();
    }

    /**
     * Limits the velocity of this physics body to the maximum speed.
     */
    private void limitVelocity(){
        if (vel.length() > maxSpeed)
            vel = vel.normalized().multiply(maxSpeed);
    }

    /**
     * Updates the physics body by applying acceleration, updating position, and resetting acceleration.
     * 
     * @param dt the time delta since the last update, in seconds.
     */
    @Override
    public void update(double dt){
        vel = vel.add(acc);
            
        limitVelocity();
        
        setLocalPos(getLocalPos().add(vel.multiply(dt)));
        acc = acc.multiply(0);
            
        if (collider == null)
            Logger.throwError(getClass(), "update: Collider is null. Did you forget to add one?");
    }

    /**
     * Handles collision with another physics body.
     * 
     * @param other the other {@link PhysicsBody} involved in the collision.
     */
    public void handleCollisionWith(PhysicsBody other){
        // Logger.logDebug(getClass(), "CHECKING COLLISIONS: " + collider + " with " + other.collider);
        CollisionInfo cInfo = collider.checkCollisionWith(other.collider);
        if (cInfo.result)
            resolveCollision(other, cInfo);
    }

    /**
     * Resolves a collision with another physics body based on collision information.
     * 
     * @param other the other {@link PhysicsBody} involved in the collision.
     * @param cInfo the {@link CollisionInfo} containing details about the collision.
     */
    private void resolveCollision(PhysicsBody other, CollisionInfo cInfo){
        // for(Vec2 contact : cInfo.contacts)
        //     GameManager.getRenderer().drawCross(contact.add(getGlobalPos()), 10, Color.RED);
    }

    /**
     * Called when this physics body collides with another collider.
     * 
     * @param other the other {@link Collider} involved in the collision.
     */
    @Override
    public void onCollision(Collider other) {}

    /**
     * Called when another collider enters this physics body's trigger.
     * 
     * @param other the other {@link Collider} that entered the trigger.
     */
    @Override
    public void onTriggerEnter(Collider other) {}

    /**
     * Called when another collider exits this physics body's trigger.
     * 
     * @param other the other {@link Collider} that exited the trigger.
     */
    @Override
    public void onTriggerExit(Collider other) {}

    /**
     * Called when this physics body is destroyed.
     */
    @Override
    public void onDeath() {
        super.onDeath();
        collider.onDestroy();
    }
}
