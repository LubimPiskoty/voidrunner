package sk.piskotka.ship;

import sk.piskotka.GameManager;
import sk.piskotka.guns.Timer;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.Projectile;

public abstract class Spaceship extends PhysicsBody{
    private Timer attackTimer;

    protected Spaceship(int x, int y, float speed, float attackSpeed) {
        super(x, y);
        this.speed = speed;
        attackTimer = new Timer(1.0/attackSpeed);
    }

    public void aim(Vec2 target){
        rotation = Math.atan2(target.getY() - pos.getY(),target.getX() - pos.getX());
    }

    @Override
    public void onUpdate(double dt) {
        attackTimer.tick(dt);
    }

    public void shoot() {
        //Logger.LogDebug("Attack timer remaining: " + attackTimer.remainingTime());
        if (attackTimer.isReady()){
            Logger.LogDebug("Ship is shooting");
            GameManager.getInstance().CreateEntity(new Projectile(pos.getX(), pos.getY(), 1000, rotation));
            attackTimer.reset();
        }
    }

    public void move(Vec2 input) {
        ApplyForce(Vec2.multiply(input, speed));
    }
}
