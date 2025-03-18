package sk.piskotka.ship;

import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;

public abstract class Spaceship extends PhysicsBody{

    protected Spaceship(int x, int y, float speed) {
        super(x, y);
        this.speed = speed;
    }

    public void aim(Vec2 target){
        rotation = Math.atan2(pos.getY() - target.getY(), pos.getX() - target.getX());
    }

    public void shoot() {
        System.out.println("SHOOTING");
    }

    public void move(Vec2 input) {
        ApplyForce(Vec2.multiply(input, speed));
    }
}
