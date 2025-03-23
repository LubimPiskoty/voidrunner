package sk.piskotka.physics;

import sk.piskotka.logger.Logger;
import sk.piskotka.render.Drawable;
import sk.piskotka.shapes.Shape;

public abstract class PhysicsBody extends Transform implements Drawable, CollisionEvents {
    int health;
    int maxHealth;

    private Collider collider;
    public Collider getCollider() {return collider;}

    private Shape shape;
    public Shape getShape() {return shape;}
    public void setShape(Shape shape) 
    {
        this.shape = shape;
        collider = new Collider(this, shape.getPoints());
    }

    protected Vec2 vel, acc;
    protected float speed;
    protected float maxSpeed;

    public PhysicsBody(int x, int y){
        this.setLocalPos(new Vec2(x, y));
        this.setLocalRot(0);
        this.health = 0;
        this.maxHealth = 0;
        this.speed = 0;
        this.maxSpeed = 500;
        this.vel = Vec2.ZERO();
        this.acc = Vec2.ZERO();
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
        return collider.checkCollisionWith(other.collider);
    }

    public void resolveCollision(PhysicsBody other){
        //TODO: Implement
    }

    @Override
    public void onCollision(Collider other) {
        //Logger.logDebug(getClass(), this + " has collided with " + other);
    }

    @Override
    public void onTriggerEnter(Collider other) {}
    @Override
    public void onTriggerExit(Collider other) {}

    @Override
    public void onDeath() {
        super.onDeath();
        collider.onDestroy();
    }
}
