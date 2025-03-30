package sk.piskotka.physics;

import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.Collider.CollisionInfo;
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

    protected Vec2 vel;
    protected float speed;
    protected float maxSpeed;

    public PhysicsBody(int x, int y){
        super(new Vec2(x, y));
        this.setLocalRot(0);
        this.health = 0;
        this.maxHealth = 0;
        this.speed = 0;
        this.maxSpeed = 500;
        this.vel = Vec2.ZERO();
    }

    public void ApplyForce(Vec2 vec){
        vel = vel.add(vec);   
    }

    public void update(double dt){
        if (vel.length() > maxSpeed)
            vel = vel.normalized().multiply(maxSpeed);

        setLocalPos(getLocalPos().add(vel.multiply(dt)));
        
        if (collider == null)
            Logger.throwError(getClass(), "update: Collider is null. Did you forget to add one?");
    }

    public void handleCollisionWith(PhysicsBody other){
        CollisionInfo cInfo = collider.checkCollisionWith(other.collider);
        if (cInfo.result)
            resolveCollision(other, cInfo);
    }

    private void resolveCollision(PhysicsBody other, CollisionInfo cInfo){
        for(Vec2 contact : cInfo.contacts)
            GameManager.getRenderer().drawCross(contact.add(getGlobalPos()), 10, Color.RED);
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
