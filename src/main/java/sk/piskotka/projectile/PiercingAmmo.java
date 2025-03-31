package sk.piskotka.projectile;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import sk.piskotka.components.Collider;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.TriangleShape;
import sk.piskotka.ship.Spaceship;

public class PiercingAmmo extends Projectile{
    private List<PhysicsBody> alreadyHit;

    public PiercingAmmo(PhysicsBody whoShotMe, int x, int y, double rotation) {
        super(whoShotMe, x, y, 1500, rotation, 2);
        setShape(new TriangleShape(0, 0, 12));
        alreadyHit = new ArrayList<>();
    }

    @Override
    public void draw(Renderer ctx) {
        ctx.drawPolygonWithTransform(this, getShape(), Color.CYAN);
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
        speed -= 500;
    }
    
}
