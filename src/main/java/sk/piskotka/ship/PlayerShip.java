package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.Projectile;
import sk.piskotka.render.Renderer;
import sk.piskotka.render.shapes.PolygonShape;
import sk.piskotka.render.shapes.Shape;
import sk.piskotka.render.shapes.TriangleShape;

public class PlayerShip extends Spaceship{
    private Shape nose;
    private Transform gunTransform;

    public PlayerShip(int x, int y) {
        super(x, y, 2000, 20);
        shape = new TriangleShape(0, 0, 50).create();
        nose = new PolygonShape(0, 0, 9, 12).create();
        
        gunTransform = new Transform();
        gunTransform.setParent(this);   
        gunTransform.setLocalPos(new Vec2(50, 0));
    }

    @Override
    public void shoot() {
        Vec2 gunPos = gunTransform.getGlobalPos(); 
        GameManager.getLevel().Create(new Projectile(gunPos.getX(), gunPos.getY(), 1000, getLocalRot()));
    }

    @Override
    public void draw(Renderer ctx) {
        Vec2 gunPos = gunTransform.getGlobalPos();
        ctx.drawPolygonWithOffset(getGlobalPos().getX(), getGlobalPos().getY(), shape.rotatedShape(getLocalRot()).getPoints(), Color.BLUE);
        ctx.drawPolygonWithOffset(gunPos.getX(), gunPos.getY(), nose.rotatedShape(getLocalRot()).getPoints(), Color.RED);
        // for(Vec2 p : shape.getPoints()){
        //     ctx.drawCross(getGlobalPos.getX()+p.getX(), getGlobalPos.getY()+p.getY(), 4, Color.RED);
        // }
    }
}
