package sk.piskotka.effects;

import sk.piskotka.physics.Vec2;

public class Particle {
    private Vec2 pos, vel;

    public Vec2 getPos() {return pos;}

    public Particle(Vec2 pos, Vec2 vel){
        this.pos = pos;
        this.vel = vel;
    }

    public void update(double dt) {
        pos = pos.add(vel.multiply(dt));
    }
}
