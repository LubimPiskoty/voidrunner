package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.components.HealthComponent;
import sk.piskotka.effects.SparksEffect;
import sk.piskotka.guns.Timer;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;

/**
 * Represents a generic spaceship in the game. This is an abstract class that defines
 * core spaceship mechanics, including movement, aiming, shooting, health management,
 * and damage handling.
 * 
 * <p>Spaceships extend {@link PhysicsBody} and incorporate a {@link HealthComponent}
 * for health tracking, along with a {@link Timer} for managing attack speed.</p>
 * 
 * <h2>Features:</h2>
 * <ul>
 *     <li>Health system with damage and healing functionality.</li>
 *     <li>Movement and velocity control.</li>
 *     <li>Attack cooldown management.</li>
 *     <li>Aiming mechanics to rotate towards a target.</li>
 *     <li>Visual effects when taking damage.</li>
 * </ul>
 * 
 * <p>Subclasses must implement the {@link #shoot()} method to define specific shooting behavior.</p>
 * 
 * @author Piskotka
 */
public abstract class Spaceship extends PhysicsBody {

    /** The spaceship's health component, managing current and maximum health. */
    protected HealthComponent health;

    /** Timer for managing attack cooldowns. */
    protected Timer attackTimer;

    /** The spaceship's movement speed. */
    protected float speed;

    /**
     * Constructs a new spaceship.
     * 
     * @param x          The initial x-coordinate.
     * @param y          The initial y-coordinate.
     * @param speed      The movement speed of the spaceship.
     * @param attackSpeed The rate of fire (shots per second).
     * @param health     The initial health of the spaceship.
     * @param maxHealth  The maximum health of the spaceship.
     */
    protected Spaceship(double x, double y, float speed, float attackSpeed, float health, float maxHealth) {
        super(x, y, 0);
        this.health = new HealthComponent(this, health, maxHealth);
        this.speed = speed;
        this.attackTimer = new Timer(1.0 / attackSpeed);
    }

    /**
     * Retrieves the spaceship's current health.
     * 
     * @return The current health value.
     */
    public double getHealth() {
        return health.getHealth();
    }

    /**
     * Retrieves the spaceship's maximum health.
     * 
     * @return The maximum health value.
     */
    public double getMaxHealth() {
        return health.getMaxHealth();
    }

    /**
     * Rotates the spaceship to aim towards a specified target.
     * 
     * @param target The target position in world coordinates.
     */
    public void aim(Vec2 target) {
        setRotation(target.subtract(getGlobalPos()).getHeading());
    }

    /**
     * Updates the spaceship's state each frame.
     * 
     * @param dt The time delta (in seconds) since the last update.
     */
    @Override
    public void update(double dt) {
        super.update(dt);
        attackTimer.tick(dt);
    }

    /**
     * Attempts to fire a shot. The spaceship will only shoot if the attack timer is ready.
     */
    public void attemptToShoot() {
        if (attackTimer.isReady()) {
            shoot();
            attackTimer.reset();
        }
    }

    /**
     * Defines the spaceship's shooting behavior. Must be implemented by subclasses.
     */
    protected abstract void shoot();

    /**
     * Moves the spaceship based on the given input direction.
     * 
     * @param input A vector representing the desired movement direction.
     */
    public void move(Vec2 input) {
        SetVelocity(input.multiply(speed));
    }

    /**
     * Reduces the spaceship's health by a specified amount and creates visual damage effects.
     * If health reaches zero, the spaceship is destroyed.
     * 
     * @param amount The amount of damage to take.
     */
    public void takeDamage(float amount) {
        health.reduceHealth(amount);
        Create(new SparksEffect(getGlobalPos(), Color.RED, 0.5, 2));

        if (health.isDead()) {
            Logger.logInfo(getClass(), "Object health was reduced to 0");

            // Generate destruction effects
            Create(new SparksEffect(getGlobalPos(), Color.RED, 2, 4));
            Create(new SparksEffect(getGlobalPos(), Color.ORANGE, 1.2, 3));

            // Destroy the spaceship
            Destroy(this);
        }
    }

    /**
     * Restores the spaceship's health by a specified amount.
     * 
     * @param amount The amount of health to restore.
     */
    public void healUp(float amount) {
        health.increaseHealth(amount);
    }
}
