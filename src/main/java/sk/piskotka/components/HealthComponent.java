package sk.piskotka.components;

import javafx.scene.paint.Color;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Renderer;

/**
 * Represents a health component for an entity, managing its health and rendering a health bar.
 */
public class HealthComponent extends Component {
    private float health;
    private final float maxHealth;

    /**
     * Gets the current health value.
     * 
     * @return the current health.
     */
    public float getHealth() { return health; }

    /**
     * Gets the maximum health value.
     * 
     * @return the maximum health.
     */
    public float getMaxHealth() { return maxHealth; }

    /**
     * Constructs a HealthComponent with the specified physics body, health, and maximum health.
     * 
     * @param pBody the physics body associated with this component.
     * @param health the initial health value.
     * @param maxHealth the maximum health value.
     * @throws IllegalArgumentException if health is greater than maxHealth, maxHealth is non-positive, or health is negative.
     */
    public HealthComponent(PhysicsBody pBody, float health, float maxHealth) {
        super(pBody);
        if (health > maxHealth || maxHealth <= 0 || health < 0)
            Logger.logError(getClass(), "Component initiated with invalid data");
        this.health = health;
        this.maxHealth = maxHealth;
    }

    /**
     * Gets the health percentage (current health divided by maximum health).
     * 
     * @return the health percentage as a float between 0 and 1.
     */
    public float getPercentage() { return health / maxHealth; }

    /**
     * Checks if the entity is dead (health is 0 or less).
     * 
     * @return true if the entity is dead, false otherwise.
     */
    public boolean isDead() { return health <= 0; }

    /**
     * Reduces the health by the specified amount, ensuring it does not drop below 0.
     * 
     * @param amount the amount to reduce health by.
     */
    public void reduceHealth(float amount) { health = Math.max(0, health - amount); }

    /**
     * Increases the health by the specified amount, ensuring it does not exceed the maximum health.
     * 
     * @param amount the amount to increase health by.
     */
    public void increaseHealth(float amount) { health = Math.min(maxHealth, health + amount); }

    /**
     * Draws a health bar at the specified position.
     * 
     * @param ctx the renderer used to draw the health bar.
     * @param position the position where the health bar should be drawn.
     */
    public void drawHealth(Renderer ctx, Vec2 position) {
        ctx.drawProgressbar(position.add(Vec2.UP().multiply(100)), 50, getPercentage(), Color.DARKGREEN, Color.LIMEGREEN);
    }
}
