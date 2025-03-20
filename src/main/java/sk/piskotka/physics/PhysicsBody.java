package sk.piskotka.physics;

import sk.piskotka.render.Drawable;
import sk.piskotka.render.shapes.Shape;

public abstract class PhysicsBody extends Transform implements Drawable{
    int health;
    int maxHealth;
    Collider collider;
    protected Vec2 vel, acc;
    protected Shape shape;
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
    }
}
