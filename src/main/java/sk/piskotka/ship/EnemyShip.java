package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.NormalAmmo;
import sk.piskotka.projectile.Projectile;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;
import sk.piskotka.shapes.TriangleShape;

public abstract class EnemyShip extends Spaceship {

    public EnemyShip(int x, int y, float speed, float attackSpeed, float health, float maxHealth) {
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
