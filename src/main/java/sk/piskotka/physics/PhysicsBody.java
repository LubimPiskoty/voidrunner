package sk.piskotka.physics;

import sk.piskotka.logger.Logger;
import sk.piskotka.physics.Collider.CollisionResult;
import sk.piskotka.render.Drawable;
import sk.piskotka.shapes.Shape;

public abstract class PhysicsBody extends Transform implements Drawable {
    int health;
    int maxHealth;

    private Collider collider;
    public Collider getCollider() {return collider;}

    private Shape shape;
    public Shape getShape() {return shape;}
    public void setShape(Shape shape) 
    {
        this.shape = shape;
        collider = new Collider(this, shape, false);
    }

    protected Vec2 vel, acc;
    protected float speed;
    protected float maxSpeed;

    public PhysicsBody(int x, int y){
        this.setLocalPos(new Vec2(x, y));
        this.setLocalRot(0);
        this.health = 0;
        this.maxHealth = 0;
        this.vel = Vec2.ZERO();
        this.acc = Vec2.ZERO();
        this.speed = -1;
        this.maxSpeed = 500;
    }

    public void ApplyForce(Vec2 vec){
        acc = acc.add(vec);   
    }

    public void update(double dt){
        vel = vel.add(acc.multiply(dt));
        if (vel.length() > maxSpeed)
            vel = vel.normalized().multiply(maxSpeed);

        setLocalPos(getLocalPos().add(vel.multiply(dt)));
        acc = Vec2.ZERO();
        
        if (collider == null)
            Logger.throwError(getClass(), "update: Collider is null. Did you forget to add one?");
    }

    public boolean checkCollisionWith(PhysicsBody other){
        if (collider.isCollidingWith(other.collider)){
            this.collider.onCollision();
            other.collider.onCollision();
            return true;
        }
        return false;
    }

    public void resolveCollision(PhysicsBody other){
        //TODO: Implement
    }
}
