package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.NormalAmmo;
import sk.piskotka.projectile.Projectile;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;

/**
 * The {@code TankEnemy} class represents an enemy ship that behaves like a tank.
 * It is a type of {@link EnemyShip} with multiple gun points positioned at each
 * vertex of the ship's shape. The tank-like enemy is capable of rotating its guns 
 * and shooting projectiles from each of its gun points.
 * 
 * <p>The {@code TankEnemy} inherits from {@link EnemyShip} and customizes its 
 * shooting mechanism to fire multiple projectiles simultaneously from different 
 * positions on the ship. The ship's rotation is also updated over time.</p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Contains multiple gun points based on the vertices of the ship's shape.</li>
 *     <li>Rotates and fires projectiles from each gun point.</li>
 *     <li>Customizes the {@code shoot()} method to fire projectiles from multiple positions.</li>
 * </ul>
 * 
 * @author Piskotka
 */
public class TankEnemy extends EnemyShip {
    
    /** Array of gun points located at each vertex of the tank enemy's shape. */
    private final Transform[] gunPoints;

    /**
     * Constructs a new {@code TankEnemy} at the specified coordinates with predefined speed, attack speed, health, and max health.
     * Initializes the gun points based on the shape's vertices.
     * 
     * @param x The x-coordinate of the enemy's position.
     * @param y The y-coordinate of the enemy's position.
     */
    public TankEnemy(int x, int y) {
        super(x, y, 0.6f, 0.5f, 15, 15);
        gunPoints = new Transform[5];
        setShape(new PolygonShape(0, 0, 50, 5));

        // Create a special gunpoint for each vertex of the ship
        int i = 0;
        for (Vec2 vertex : getShape().getPoints()) {
            gunPoints[i] = new Transform(vertex, this);
            gunPoints[i].setRotation(vertex.getHeading());
            i++;
        }
    }

    /**
     * Fires projectiles from each gun point of the tank enemy.
     * Each gun point fires a {@link NormalAmmo} projectile in the direction 
     * the gun point is facing.
     */
    @Override
    protected void shoot() {
        for (Transform gunPoint : gunPoints) {
            Vec2 gunPos = gunPoint.getGlobalPos();
            Projectile p = new NormalAmmo(this, gunPos.getX(), gunPos.getY(), gunPoint.getRotation());
            GameManager.getLevel().create(p);
        }
    }

    /**
     * Updates the tank enemy's behavior. This includes rotating the ship over time
     * based on the enemy's speed and the elapsed time (dt).
     * 
     * @param dt The delta time (time elapsed) since the last update.
     */
    @Override
    public void update(double dt) {
        super.update(dt);
        setRotation(getRotation() + speed * dt);
    }

    /**
     * Renders the tank enemy on the screen. This includes drawing the ship shape
     * in dark green and rendering arrows at each of the gun points to indicate
     * where the projectiles will be fired from.
     * 
     * @param ctx The renderer used to draw the tank enemy and its components.
     */
    @Override
    public void draw(Renderer ctx) {
        super.draw(ctx);
        ctx.drawShape(this, getShape(), Color.DARKGREEN);
        for (Transform gunPoint : gunPoints) {
            ctx.drawArrow(gunPoint.getGlobalPos(), Vec2.fromHeading(gunPoint.getRotation()).multiply(20), Color.GREEN);
        }
    }
}
