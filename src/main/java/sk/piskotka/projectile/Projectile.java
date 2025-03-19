package sk.piskotka.projectile;

import javafx.scene.paint.Color;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Renderer;
import sk.piskotka.render.Shape;

public class Projectile extends PhysicsBody{

    public Projectile(int x, int y, float speed, double rotation) {
        super(x, y);
        this.shape = Shape.CreateRectangle(-4, -2, 8, 4);
        this.speed = speed;
        this.rotation = rotation;
        //TODO: Fix bug with shooting into the other side
        this.vel = Vec2.FromHeading(rotation);
        this.vel.multiply(-speed);
    }
    
    @Override
    public void draw(Renderer ctx) {
        ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), this.shape.rotatedShape(rotation).getPoints(), Color.CRIMSON);
    }

    @Override
    public void onUpdate(double dt) {
    }
    
}
