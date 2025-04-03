package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.NormalAmmo;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;
import sk.piskotka.shapes.Shape;

/**
 * The {@code PlayerShip} class represents the player's spaceship in the game.
 * It extends the {@link Spaceship} class, adding specific behavior and rendering
 * for the player's ship, including movement, shooting, and health management.
 * 
 * <p>The player ship is represented by a polygonal shape, and it is capable of
 * shooting projectiles (specifically {@link NormalAmmo}). The ship also has a nose
 * shape to represent its front, with a transform used to control the position of
 * the ship's weapon.</p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Can shoot {@link NormalAmmo} projectiles.</li>
 *     <li>Has a defined nose shape that is used to represent the front of the ship.</li>
 *     <li>Displays health information through the {@code health} component.</li>
 *     <li>Renders the ship and its weapon using the provided {@link Renderer}.</li>
 * </ul>
 * 
 * @author Piskotka
 */
public class PlayerShip extends Spaceship {

    /** Shape representing the nose of the player ship. */
    private Shape nose;

    /** Transform used to control the position and rotation of the player's weapon. */
    private Transform gunTransform;

    /**
     * Constructs a {@code PlayerShip} with the specified position, health, and maximum health.
     * The ship is initialized with a movement speed of 1000 and an attack speed of 5.
     * It also sets up the shape of the ship and the transform for the weapon's position.
     * 
     * @param x The x-coordinate of the ship's position.
     * @param y The y-coordinate of the ship's position.
     * @param health The current health of the ship.
     * @param maxHealth The maximum health of the ship.
     */
    public PlayerShip(int x, int y, float health, float maxHealth) {
        super(x, y, 1000, 5, health, maxHealth);
        setShape(new PolygonShape(0, 0, 50, 3));
        nose = new PolygonShape(0, 0, 9, 4);
        
        // Set up the transform for the gun's position
        gunTransform = new Transform(new Vec2(50, 0), this);
    }

    /**
     * Shoots a projectile from the player ship. The projectile is created at the
     * position of the gun (calculated using the {@code gunTransform}) and fired
     * in the direction the ship is facing.
     */
    @Override
    public void shoot() {
        Vec2 gunPos = gunTransform.getGlobalPos();
        // Create a new NormalAmmo instance to represent the shot
        Create(new NormalAmmo(this, gunPos.getX(), gunPos.getY(), getRotation()));
    }

    /**
     * Renders the player ship and its components (nose and health) to the screen
     * using the provided {@link Renderer}.
     * 
     * @param ctx The renderer used to draw the ship and its components.
     */
    @Override
    public void draw(Renderer ctx) {
        // Draw the main shape of the ship and its nose
        ctx.drawShape(this, getShape(), Color.BLUE);
        ctx.drawShape(gunTransform, nose, Color.BLUEVIOLET);
        // Draw the health status of the ship
        health.drawHealth(ctx, getGlobalPos());
    }
}
