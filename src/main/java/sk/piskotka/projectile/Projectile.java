package sk.piskotka.projectile;

import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.components.Collider;
import sk.piskotka.effects.SparksEffect;
import sk.piskotka.guns.Timer;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;
import sk.piskotka.ship.Spaceship;

/**
 * Represents a projectile in the game. A projectile is a physics body
 * that moves in a specific direction, deals damage upon collision, and
 * has a limited lifespan.
 */
public abstract class Projectile extends PhysicsBody {
    protected Timer deathTimer;
    protected PhysicsBody whoShotMe;
    protected float damage;

    /**
     * Constructs a new Projectile.
     *
     * @param whoShotMe The physics body that fired this projectile.
     * @param x The initial x-coordinate of the projectile.
     * @param y The initial y-coordinate of the projectile.
     * @param speed The speed of the projectile.
     * @param rotation The rotation angle of the projectile in radians.
     * @param damage The amount of damage the projectile deals upon collision.
     */
    public Projectile(PhysicsBody whoShotMe, double x, double y, float speed, double rotation, float damage) {
        super(x, y, rotation);
        this.speed = speed;
        this.maxSpeed = speed;
        this.damage = damage;
        this.whoShotMe = whoShotMe;
        this.setShape(new PolygonShape(0, 0, 10, 4));
        setVelocity(Vec2.fromHeading(rotation).multiply(speed));
        this.deathTimer = new Timer(2);
    }

    /**
     * Updates the state of the projectile. This includes ticking the death timer
     * and destroying the projectile if its lifespan has ended.
     *
     * @param dt The time delta since the last update, in seconds.
     */
    @Override
    public void update(double dt) {
        super.update(dt);
        deathTimer.tick(dt);
        if (deathTimer.isReady())
            GameManager.getLevel().destroy(this);
    }
    
    /**
     * Draws the projectile on the screen. This method is currently commented out.
     *
     * @param ctx The renderer used to draw the projectile.
     */
    @Override
    public void draw(Renderer ctx) {
        // Vec2 global = getGlobalPos();
        // ctx.drawPolygonWithOffset(global.getX(), global.getY(), getShape().rotated(getLocalRot()).getPoints(), Color.PINK);
    }

    /**
     * Handles the collision of the projectile with another collider.
     * If the projectile collides with a spaceship, it deals damage to it.
     * The projectile is destroyed after the collision.
     *
     * @param other The collider that the projectile collided with.
     */
    @Override
    public void onCollision(Collider other) {
        if (other.getPhysicsBody() == whoShotMe)
            return; // Ignore the collision with the shooter of this projectile

        if (other.getPhysicsBody() instanceof Spaceship ship){
            Logger.logInfo(getClass(), "Projectile hit ship: " + ship.toString() + " for " + damage + " damage");
            ship.takeDamage(damage);
        }

        Create(new SparksEffect(getGlobalPos(), Color.WHITE, 0.7, 1));
        Destroy(this);
    }
}
