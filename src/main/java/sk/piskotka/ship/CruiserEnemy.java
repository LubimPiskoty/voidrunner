package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.PiercingAmmo;
import sk.piskotka.projectile.Projectile;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.TriangleShape;

public class CruiserEnemy extends EnemyShip {
    private double aimSpeed;
    private Transform nose;

    public CruiserEnemy(double x, double y) {
        super(x, y, 100, 1, 5, 5);
        aimSpeed = 0.5;
        int size = 30;
        setShape(new TriangleShape(-10, 0, size));
        nose = new Transform(Vec2.RIGHT().multiply(size).subtract(new Vec2(size/2, 0)), this);
    }

    @Override
    public void update(double dt) {
        // Aim towards player
        Vec2 playerDir = GameManager.getLevel().getPlayer().getGlobalPos().subtract(getGlobalPos()).normalized();
        Vec2 forward = Vec2.UP().rotated(getRotation()).normalized();
        // Lineary interpolate towards player
        double lerp = playerDir.subtract(forward).multiply(aimSpeed*dt).getHeading() + Math.PI/4;
        setRotation(lerp);
        
        attemptToShoot();
        super.update(dt);
    }

    @Override
    protected void shoot() {
        Projectile p = new PiercingAmmo(this, getGlobalPos().getX(), getGlobalPos().getY(), getRotation());
        Create(p);
    }

    @Override
    public void draw(Renderer ctx) {
        super.draw(ctx);
        ctx.drawShape(this, getShape(), Color.DARKBLUE);
        ctx.drawArrow(nose.getGlobalPos(), nose.forward().multiply(50), Color.BLUE);
    }
    
}
