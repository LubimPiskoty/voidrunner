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

public class PiercingAmmo extends Projectile{
    private List<PhysicsBody> alreadyHit;

    public PiercingAmmo(PhysicsBody whoShotMe, double x, double y, double rotation) {
        super(whoShotMe, x, y, 1500, rotation, 2);
        setShape(new TriangleShape(0, 0, 10));
        alreadyHit = new ArrayList<>();
    }

    @Override
    public void draw(Renderer ctx) {
        ctx.drawShape(this, getShape(), Color.CYAN);
    }

    @Override
    public void onCollision(Collider other) {
        if (other.getPhysicsBody() == whoShotMe || alreadyHit.contains(other.getPhysicsBody()))
            return; // Ignore the collision with the shooter of this projectile

        if (other.getPhysicsBody() instanceof Spaceship){
            Spaceship ship = (Spaceship)other.getPhysicsBody();
            Logger.logInfo(getClass(), "Projectile hit ship: " + ship.toString() + " for " + damage + " damage");
            ship.takeDamage(damage);
        }

        //TODO: Change this into on enter etc
        alreadyHit.add(other.getPhysicsBody());
        setVelocity(getVelocity().multiply(0.65));
        damage /= 2;
        deathTimer.tick(0.6);
        Create(new SparksEffect(getGlobalPos(), Color.CYAN, deathTimer.remainingTime()/2, 1));

    }
    
}
