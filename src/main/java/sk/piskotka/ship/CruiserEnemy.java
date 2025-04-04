package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.PiercingAmmo;
import sk.piskotka.projectile.Projectile;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;

/**
 * The {@code CruiserEnemy} class represents an enemy cruiser ship that moves and aims
 * towards the player's position while shooting piercing projectiles.
 * <p>
 * The cruiser ship has a specific aiming behavior that allows it to gradually rotate 
 * towards the player's position and shoot piercing projectiles at regular intervals.
 * </p>
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Moves and rotates smoothly towards the player's position.</li>
 *     <li>Shoots piercing projectiles in the direction it is facing.</li>
 *     <li>Rendered with a distinct triangular shape and aiming nose.</li>
 * </ul>
 * 
 * @author Piskotka
 */
public class CruiserEnemy extends EnemyShip {
    private double aimSpeed;
    private final Transform nose;

    /**
     * Constructs a new {@code CruiserEnemy} at the specified position.
     * 
     * @param x The x-coordinate of the cruiser enemy's position.
     * @param y The y-coordinate of the cruiser enemy's position.
     */
    public CruiserEnemy(double x, double y) {
        super(x, y, 100, 1, 5, 5);  // Calls the constructor of EnemyShip with specific parameters
        aimSpeed = 0.5; // Speed at which the enemy aims towards the player
        int size = 40;  // Size of the cruiser enemy
        setShape(new PolygonShape(-size / 2, 0, size, 8));
        nose = new Transform(Vec2.RIGHT().multiply(size).subtract(new Vec2(size / 2, 0)), this); // Position the nose of the cruiser
    }

    /**
     * Updates the cruiser enemy's behavior. This includes rotating towards the player
     * and attempting to shoot at regular intervals.
     * <p>
     * The ship gradually aims towards the player by linearly interpolating its rotation
     * and then attempts to shoot.
     * </p>
     * 
     * @param dt The delta time (time elapsed) since the last update.
     */
    @Override
    public void update(double dt) {
        // Aim towards player
        Vec2 playerDir = GameManager.getLevel().getPlayer().getGlobalPos().subtract(getGlobalPos()).normalized();
        Vec2 forward = Vec2.UP().rotated(getRotation()).normalized();
        
        // Linearly interpolate towards player direction
        double lerp = playerDir.subtract(forward).multiply(aimSpeed * dt).getHeading() + Math.PI / 4;
        setRotation(lerp);
        
        attemptToShoot();  // Attempt to shoot
        super.update(dt);  // Call the parent class update method
    }

    /**
     * Shoots a piercing projectile in the direction the cruiser is facing.
     * The projectile is created and added to the level.
     */
    @Override
    protected void shoot() {
        Projectile p = new PiercingAmmo(this, getGlobalPos().getX(), getGlobalPos().getY(), getRotation());
        Create(p);  // Create the projectile and add it to the game world
    }

    /**
     * Draws the cruiser enemy on the screen. This method renders the cruiser ship's shape, 
     * the aiming nose, and the ship's health.
     * 
     * @param ctx The renderer used to draw the cruiser ship on the screen.
     */
    @Override
    public void draw(Renderer ctx) {
        super.draw(ctx);  // Draw health and other basic elements
        ctx.drawShape(this, getShape(), Color.DARKBLUE);  // Draw the cruiser ship itself
        ctx.drawArrow(nose.getGlobalPos(), nose.forward().multiply(50), Color.BLUE);  // Draw the aiming nose of the cruiser
    }
}
