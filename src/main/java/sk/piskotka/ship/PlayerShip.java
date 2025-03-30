package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.Projectile;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;
import sk.piskotka.shapes.Shape;
import sk.piskotka.shapes.TriangleShape;

public class PlayerShip extends Spaceship{
    private Shape nose;
    private Transform gunTransform;

    public PlayerShip(int x, int y) {
        super(x, y, 20, 5);
        setShape(new PolygonShape(0, 0, 50, 3));
        nose = new PolygonShape(0, 0, 9, 4);
        
        gunTransform = new Transform(new Vec2(50, 0), this);
    }

    @Override
    public void shoot() {
        Vec2 gunPos = gunTransform.getGlobalPos(); 
        GameManager.getLevel().create(new Projectile(gunPos.getX(), gunPos.getY(), 1000, getLocalRot()));
    }

    @Override
    public void draw(Renderer ctx) {
        ctx.drawPolygonWithTransform(this, getShape(), Color.BLUE);
        ctx.drawPolygonWithTransform(gunTransform, nose, Color.BLUEVIOLET);
        // for(Vec2 p : shape.getPoints()){
        //     ctx.drawCross(getGlobalPos.getX()+p.getX(), getGlobalPos.getY()+p.getY(), 4, Color.RED);
        // }
    }
}
