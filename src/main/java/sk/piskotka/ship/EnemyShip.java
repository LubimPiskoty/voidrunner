package sk.piskotka.ship;

import sk.piskotka.render.Renderer;

/**
 * The {@code EnemyShip} class represents a generic enemy spaceship in the game.
 * It is a subclass of {@link Spaceship} and provides additional behavior specific
 * to enemy ships, such as the ability to attempt to shoot at regular intervals.
 * 
 * <p>The {@code EnemyShip} class inherits from {@link Spaceship} and implements 
 * its own {@code update()} method, which calls {@code attemptToShoot()} to 
 * handle shooting attempts during the game loop.</p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Handles shooting attempts using the {@code attemptToShoot()} method.</li>
 *     <li>Manages health rendering through the {@code draw()} method.</li>
 * </ul>
 * 
 * @author Piskotka
 */
public abstract class EnemyShip extends Spaceship {

    /**
     * Constructs a new {@code EnemyShip} with the specified position, speed, 
     * attack speed, health, and maximum health.
     * 
     * @param x The x-coordinate of the enemy ship's position.
     * @param y The y-coordinate of the enemy ship's position.
     * @param speed The movement speed of the enemy ship.
     * @param attackSpeed The rate at which the enemy ship can attack.
     * @param health The current health of the enemy ship.
     * @param maxHealth The maximum health of the enemy ship.
     */
    public EnemyShip(double x, double y, float speed, float attackSpeed, float health, float maxHealth) {
        super(x, y, speed, attackSpeed, health, maxHealth);
    }

    /**
     * Updates the enemy ship's behavior. This includes calling the {@code attemptToShoot()}
     * method to allow the enemy to shoot at regular intervals.
     * 
     * @param dt The delta time (time elapsed) since the last update.
     */
    @Override
    public void update(double dt) {
        super.update(dt);
        attemptToShoot();
    }

    /**
     * Draws the enemy ship on the screen. This method renders the enemy ship's health 
     * by calling the {@code drawHealth()} method of the health component.
     * 
     * @param ctx The renderer used to draw the enemy ship's health on the screen.
     */
    @Override
    public void draw(Renderer ctx) {
        health.drawHealth(ctx, getGlobalPos());
    }
}
