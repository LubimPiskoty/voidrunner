package sk.piskotka.projectile;

import javafx.scene.paint.Color;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;

/**
 * Represents a normal ammunition projectile in the game.
 * This projectile is shot by a physics body and has a polygonal shape.
 */
public class NormalAmmo extends Projectile {

    /**
     * Constructs a new NormalAmmo instance.
     *
     * @param whoShotMe The physics body that shot this projectile.
     * @param x The initial x-coordinate of the projectile.
     * @param y The initial y-coordinate of the projectile.
     * @param rotation The initial rotation of the projectile in degrees.
     */
    public NormalAmmo(PhysicsBody whoShotMe, double x, double y, double rotation) {
        super(whoShotMe, x, y, 1000, rotation, 1);
        setShape(new PolygonShape(0, 0, 10, 8));
    }

    /**
     * Draws the projectile using the provided renderer.
     *
     * @param ctx The renderer used to draw the projectile.
     */
    @Override
    public void draw(Renderer ctx) {
        ctx.drawShape(this, getShape(), Color.DIMGRAY);
    }
}
