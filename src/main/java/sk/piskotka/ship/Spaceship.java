package sk.piskotka.ship;

import sk.piskotka.components.HealthComponent;
import sk.piskotka.guns.Timer;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;

public abstract class Spaceship extends PhysicsBody{
    protected HealthComponent health;
    protected Timer attackTimer;

    protected Spaceship(double x, double y, float speed, float attackSpeed, float health, float maxHealth) {
        super(x, y, 0);
        this.health = new HealthComponent(this, health, maxHealth);
        this.speed = speed;
        this.attackTimer = new Timer(1.0/attackSpeed);
    }

    public void aim(Vec2 target){
        setRotation(target.subtract(getGlobalPos()).getHeading());
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
            Destroy(this);
        }
    }

    public void healUp(float amount){
        health.increaseHealth(amount);
    }
}
