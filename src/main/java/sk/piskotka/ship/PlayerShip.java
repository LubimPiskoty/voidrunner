package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.render.Renderer;
import sk.piskotka.render.shapes.Shape;
import sk.piskotka.render.shapes.TriangleShape;

public class PlayerShip extends Spaceship{
    private Shape nose; 

    public PlayerShip(int x, int y) {
        super(x, y, 2000, 20);
        shape = new TriangleShape(0, 0, 50).create();
        nose = new TriangleShape(40, 0, 9).create();
    }

    @Override
    public void draw(Renderer ctx) {
        ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), shape.rotatedShape(rotation).getPoints(),Color.BLUE);
        ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), nose.rotatedShape(rotation).getPoints(),Color.CYAN);
        // for(Vec2 p : shape.getPoints()){
        //     ctx.drawCross(pos.getX()+p.getX(), pos.getY()+p.getY(), 4, Color.RED);
        // }
    }
}
