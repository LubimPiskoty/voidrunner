package sk.piskotka.projectile;

import javafx.scene.paint.Color;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Renderer;
import sk.piskotka.render.shapes.RectangleShape;

public class Projectile extends PhysicsBody{

    public Projectile(int x, int y, float speed, double rotation) {
        super(x, y);
        this.shape = new RectangleShape(-4, -2, 8, 4).create();
        this.speed = speed;
        this.rotation = rotation;
        this.vel = Vec2.fromHeading(rotation).multiply(speed);
        this.maxSpeed = 1000;
    }
    
    @Override
    public void draw(Renderer ctx) {
        ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), this.shape.rotatedShape(rotation).getPoints(), Color.CRIMSON);
    }
}
