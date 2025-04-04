package sk.piskotka.effects;

import sk.piskotka.guns.Timer;
import sk.piskotka.physics.Vec2;

public class Particle {
    private Vec2 pos, vel;
    private final Timer lifetime;
    
    public Vec2 getPos() {return pos;}

    public Particle(Vec2 pos, Vec2 vel, double lifetime){
        this.pos = pos;
        this.vel = vel;
        this.lifetime = new Timer(lifetime);
    }

    public void update(double dt) {
        pos = pos.add(vel.multiply(dt));
        lifetime.tick(dt);
    }

    public double getLifePercentage(){
        return lifetime.completion();
    }
}
