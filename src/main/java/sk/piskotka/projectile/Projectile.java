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

public abstract class Projectile extends PhysicsBody{
    protected Timer deathTimer;
    protected PhysicsBody whoShotMe;
    protected float damage;

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

    @Override
    public void update(double dt) {
        super.update(dt);
        deathTimer.tick(dt);
        if (deathTimer.isReady())
            GameManager.getLevel().destroy(this);
    }
    
    @Override
    public void draw(Renderer ctx) {
        // Vec2 global = getGlobalPos();
        // ctx.drawPolygonWithOffset(global.getX(), global.getY(), getShape().rotated(getLocalRot()).getPoints(), Color.PINK);
    }

    @Override
    public void onCollision(Collider other) {
        if (other.getPhysicsBody() == whoShotMe)
            return; // Ignore the collision with the shooter of this projectile

        if (other.getPhysicsBody() instanceof Spaceship){
            Spaceship ship = (Spaceship)other.getPhysicsBody();
            Logger.logInfo(getClass(), "Projectile hit ship: " + ship.toString() + " for " + damage + " damage");
            ship.takeDamage(damage);
        }

        Create(new SparksEffect(getGlobalPos(), Color.WHITE, 0.7, 1));
        Destroy(this);
    }
}
