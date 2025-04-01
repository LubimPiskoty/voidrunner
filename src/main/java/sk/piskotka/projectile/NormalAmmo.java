package sk.piskotka.projectile;

import javafx.scene.paint.Color;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;

public class NormalAmmo extends Projectile{

    public NormalAmmo(PhysicsBody whoShotMe, double x, double y, double rotation) {
        super(whoShotMe, x, y, 1000, rotation, 1);
        setShape(new PolygonShape(0, 0, 10, 8));
    }

    @Override
    public void draw(Renderer ctx) {
        ctx.drawShape(this, getShape(), Color.DIMGRAY);
    }
    
}
