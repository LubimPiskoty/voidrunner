package sk.piskotka.physics;

import sk.piskotka.render.Drawable;
import sk.piskotka.render.Shape;

public abstract class PhysicsBody implements Drawable{
    int health;
    int maxHealth;
    Collider collider;
    protected double rotation;
    protected Vec2 pos, vel, acc;
    protected Shape shape;
    protected float speed;
    protected float maxSpeed;

    public PhysicsBody(int x, int y){
        this.health = 0;
        this.maxHealth = 0;
        this.pos = new Vec2(x, y);
        this.vel = Vec2.ZERO();
        this.acc = Vec2.ZERO();
        this.rotation = 0;
        this.speed = -1;
        this.maxSpeed = 500;
        this.shape = Shape.CreateEmpty();
    }

    public void ApplyForce(Vec2 vec){
        this.acc.add(vec);   
    }

    public void update(double dt){
        // System.out.println("pos: " + pos.toString());
        // System.out.println("vel: " + vel.toString());
        // System.out.println("acc: " + acc.toString());
        vel.add(Vec2.multiply(acc, dt));
        if (vel.length() > maxSpeed){
            vel.normalize();
            vel.multiply(maxSpeed);
        }

        pos.add(Vec2.multiply(vel, dt));
        acc.set(Vec2.ZERO());
        onUpdate(dt);
    }

    public abstract void onUpdate(double dt);
}
