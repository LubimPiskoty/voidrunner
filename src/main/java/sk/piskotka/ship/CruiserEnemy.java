package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.PiercingAmmo;
import sk.piskotka.projectile.Projectile;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.TriangleShape;

public class CruiserEnemy extends EnemyShip {
    private double aimSpeed;

    public CruiserEnemy(double x, double y) {
        super(x, y, 100, 1, 5, 5);
        aimSpeed = 0.5;
        setShape(new TriangleShape(0, 0, 40));
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
    }
    
}
