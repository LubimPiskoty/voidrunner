package sk.piskotka.ship;

import sk.piskotka.Physics.GameObject;
import sk.piskotka.Physics.Vec2;

public abstract class Spaceship extends GameObject{
    float speed;

    protected Spaceship(float speed) {
        this.speed = speed;
    }

    public void shoot() {
        System.out.println("SHOOTING");
    }

    public void move(Vec2 input) {
        ApplyForce(Vec2.multiply(input, speed));
    }
}
