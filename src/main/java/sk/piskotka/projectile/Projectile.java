package sk.piskotka.projectile;

import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.guns.Timer;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;
import sk.piskotka.shapes.RectangleShape;

public class Projectile extends PhysicsBody{
    private Timer deathTimer;

    public Projectile(int x, int y, float speed, double rotation) {
        super(x, y);
        this.setShape(new PolygonShape(0, 0, 10, 4));
        this.speed = speed;
        this.setLocalRot(rotation);
        this.vel = Vec2.fromHeading(rotation).multiply(speed);
        this.maxSpeed = 1000;
        this.deathTimer = new Timer(2);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        deathTimer.tick(dt);
        if (deathTimer.isReady())
            GameManager.getLevel().Destroy(this);
    }
    
    @Override
    public void draw(Renderer ctx) {
        Vec2 global = getGlobalPos();
        ctx.drawPolygonWithOffset(global.getX(), global.getY(), getShape().rotated(getLocalRot()).getPoints(), Color.CRIMSON);
    }
}
