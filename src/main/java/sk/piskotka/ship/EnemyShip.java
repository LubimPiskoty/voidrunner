package sk.piskotka.ship;

import sk.piskotka.render.Renderer;

public abstract class EnemyShip extends Spaceship {

    public EnemyShip(double x, double y, float speed, float attackSpeed, float health, float maxHealth) {
        super(x, y, speed, attackSpeed, health, maxHealth);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        attemptToShoot();
    }

    @Override
    public void draw(Renderer ctx) {
        health.drawHealth(ctx, getGlobalPos());
    }
    
}
