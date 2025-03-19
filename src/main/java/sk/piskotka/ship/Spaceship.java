package sk.piskotka.ship;

import sk.piskotka.GameManager;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.Projectile;

public abstract class Spaceship extends PhysicsBody{
    //TODO: Shooting speed

    protected Spaceship(int x, int y, float speed) {
        super(x, y);
        this.speed = speed;
    }

    public void aim(Vec2 target){
        rotation = Math.atan2(pos.getY() - target.getY(), pos.getX() - target.getX());
    }

    public void shoot() {
        Logger.LogDebug("Ship is shooting");
        GameManager.getInstance().CreateEntity(new Projectile(pos.getX(), pos.getY(), 1000, rotation));
    }

    public void move(Vec2 input) {
        ApplyForce(Vec2.multiply(input, speed));
    }
}
