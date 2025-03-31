package sk.piskotka.ship;

import sk.piskotka.GameManager;
import sk.piskotka.components.Collider;
import sk.piskotka.components.HealthComponent;
import sk.piskotka.guns.Timer;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.Projectile;
import sk.piskotka.render.Renderer;

public abstract class Spaceship extends PhysicsBody{
    protected HealthComponent health;
    protected Timer attackTimer;

    protected Spaceship(int x, int y, float speed, float attackSpeed, float health, float maxHealth) {
        super(x, y);
        this.health = new HealthComponent(this, health, maxHealth);
        this.speed = speed;
        this.attackTimer = new Timer(1.0/attackSpeed);
    }

    public void aim(Vec2 target){
        setLocalRot(target.subtract(getGlobalPos()).getHeading());
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

    
    public void takeDamage(float amount){
        health.reduceHealth(amount);
        if (health.isDead()){
            Logger.logInfo(getClass(), "Object health was reduced to 0");
            GameManager.getLevel().destroy(this);
        }
    }

    public void healUp(float amount){
        health.increaseHealth(amount);
    }
}
