package sk.piskotka.physics;

import sk.piskotka.components.Collider;
import sk.piskotka.components.Collider.CollisionInfo;
import sk.piskotka.logger.Logger;
import sk.piskotka.render.Drawable;
import sk.piskotka.shapes.Shape;

public abstract class PhysicsBody extends Transform implements Drawable, CollisionEvents {
    int health;
    int maxHealth;

    public Collider collider;
    public Collider getCollider() {return collider;}

    private Shape shape;
    public Shape getShape() {return shape;}
    public void setShape(Shape shape) 
    {
        this.shape = shape;
        collider = new Collider(this, shape.getPoints());
    }

    private Vec2 vel, acc;
    public Vec2 getVelocity() {return vel;}
    public void setVelocity(Vec2 vel) {this.vel = vel;}

    protected float speed;
    protected float maxSpeed;

    public PhysicsBody(double x, double y, double rotation){
        super(new Vec2(x, y));
        this.setRotation(rotation);
        this.health = 0;
        this.maxHealth = 0;
        this.speed = 0;
        this.maxSpeed = 350;
        this.acc = Vec2.ZERO();
        this.vel = Vec2.ZERO();
    }

    public void ApplyForce(Vec2 vec){
        acc = acc.add(vec);   
    }

    public void update(double dt){
    vel = vel.add(acc);
        
    if (vel.length() > maxSpeed)
    vel = vel.normalized().multiply(maxSpeed);
    
    setLocalPos(getLocalPos().add(vel.multiply(dt)));
    acc = acc.multiply(0);
        
    if (collider == null)
        Logger.throwError(getClass(), "update: Collider is null. Did you forget to add one?");
    }

    public void handleCollisionWith(PhysicsBody other){
        // Logger.logDebug(getClass(), "CHECKING COLLISIONS: " + collider + " with " + other.collider);
        CollisionInfo cInfo = collider.checkCollisionWith(other.collider);
        if (cInfo.result)
            resolveCollision(other, cInfo);
    }

    private void resolveCollision(PhysicsBody other, CollisionInfo cInfo){
        // for(Vec2 contact : cInfo.contacts)
        //     GameManager.getRenderer().drawCross(contact.add(getGlobalPos()), 10, Color.RED);
    }

    @Override
    public void onCollision(Collider other) {}

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
