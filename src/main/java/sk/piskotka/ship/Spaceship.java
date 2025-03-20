package sk.piskotka.ship;

import sk.piskotka.GameManager;
import sk.piskotka.guns.Timer;
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
        //TODO: Extract into function in Vec2
        setLocalRot(Math.atan2(target.getY() - getGlobalPos().getY(), target.getX() - getGlobalPos().getX()));
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        attackTimer.tick(dt);
    }

    public void attemptToShoot(){
        if (attackTimer.isReady()){
            shoot();
            attackTimer.reset();
        }
    }

    protected abstract void shoot();

    public void move(Vec2 input) {
        ApplyForce(input.multiply(speed));
    }
}
