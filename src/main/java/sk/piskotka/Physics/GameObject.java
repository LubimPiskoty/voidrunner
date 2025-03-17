package sk.piskotka.Physics;

import sk.piskotka.render.Drawable;

public abstract class GameObject implements Drawable{
    int health;
    int maxHealth;
    protected Vec2 pos, vel, acc;
    float rotation;
    Collider collider;

    public GameObject(){
        this.health = 0;
        this.maxHealth = 0;
        this.pos = Vec2.ZERO();
        this.vel = Vec2.ZERO();
        this.acc = Vec2.ZERO();
        this.rotation = 0;
    }

    public void ApplyForce(Vec2 vec){
        this.acc.add(vec);   
    }

    public void update(double dt){
        // System.out.println("pos: " + pos.toString());
        // System.out.println("vel: " + vel.toString());
        // System.out.println("acc: " + acc.toString());
        vel.add(Vec2.multiply(acc, dt));
        pos.add(Vec2.multiply(vel, dt));
        acc.set(Vec2.ZERO());
        vel.multiply(1.0-(0.8 * dt));
    }
}
