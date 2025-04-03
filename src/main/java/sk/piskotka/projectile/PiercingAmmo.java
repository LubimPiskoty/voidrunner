package sk.piskotka.projectile;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import sk.piskotka.components.Collider;
import sk.piskotka.effects.SparksEffect;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.TriangleShape;
import sk.piskotka.ship.Spaceship;

/**
 * Represents a piercing projectile that can hit multiple targets.
 * The projectile reduces its damage and velocity after each hit.
 */
public class PiercingAmmo extends Projectile {
    private final List<PhysicsBody> alreadyHit;

    /**
     * Constructs a new PiercingAmmo instance.
     *
     * @param whoShotMe The PhysicsBody that fired this projectile.
     * @param x         The initial x-coordinate of the projectile.
     * @param y         The initial y-coordinate of the projectile.
     * @param rotation  The initial rotation angle of the projectile.
     */
    public PiercingAmmo(PhysicsBody whoShotMe, double x, double y, double rotation) {
        super(whoShotMe, x, y, 1500, rotation, 2);
        setShape(new TriangleShape(0, 0, 10));
        alreadyHit = new ArrayList<>();
    }

    /**
     * Draws the projectile using the specified renderer.
     *
     * @param ctx The renderer used to draw the projectile.
     */
    @Override
    public void draw(Renderer ctx) {
        ctx.drawShape(this, getShape(), Color.CYAN);
    }

    /**
     * Handles the collision logic for the projectile.
     * If the projectile hits a spaceship, it deals damage and reduces its own velocity and damage.
     *
     * @param other The collider that this projectile collided with.
     */
    @Override
    public void onCollision(Collider other) {
        if (other.getPhysicsBody() == whoShotMe || alreadyHit.contains(other.getPhysicsBody()))
            return; // Ignore the collision with the shooter of this projectile

        if (other.getPhysicsBody() instanceof Spaceship ship) {
            Logger.logInfo(getClass(), "Projectile hit ship: " + ship.toString() + " for " + damage + " damage");
            ship.takeDamage(damage);
        }

        //TODO: Change this into on enter etc
        alreadyHit.add(other.getPhysicsBody());
        setVelocity(getVelocity().multiply(0.65));
        damage /= 2;
        deathTimer.tick(0.6);
        Create(new SparksEffect(getGlobalPos(), Color.CYAN, deathTimer.remainingTime() / 2, 1));
    }
}
