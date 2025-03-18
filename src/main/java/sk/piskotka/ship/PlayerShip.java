package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.render.Renderer;
import sk.piskotka.render.Shape;

public class PlayerShip extends Spaceship{
    private double offset = Math.PI/3;

    public PlayerShip(int x, int y) {
        super(x, y, 1000);
        shape = Shape.CreateNGon(0, 0, 50, 3);
    }


    @Override
    public void draw(Renderer ctx) {
        ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), shape.rotatedShape(rotation+offset).getPoints(),Color.BLUE);
        // for(Vec2 p : shape.getPoints()){
        //     ctx.drawCross(pos.getX()+p.getX(), pos.getY()+p.getY(), 4, Color.RED);
        // }
    }


    @Override
    public void onUpdate(double dt) {}
    
}
