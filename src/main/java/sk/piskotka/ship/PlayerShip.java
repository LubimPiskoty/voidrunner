package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.render.Renderer;
import sk.piskotka.render.Shape;

public class PlayerShip extends Spaceship{
    private Shape nose; 

    public PlayerShip(int x, int y) {
        super(x, y, 2000, 4);
        shape = Shape.CreateNGon(0, 0, 50, 3);
        nose = Shape.CreateNGon(40, 0, 9, 3);
    }

    @Override
    public void draw(Renderer ctx) {
        ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), shape.rotatedShape(rotation).getPoints(),Color.BLUE);
        ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), nose.rotatedShape(rotation).getPoints(),Color.CYAN);
        // for(Vec2 p : shape.getPoints()){
        //     ctx.drawCross(pos.getX()+p.getX(), pos.getY()+p.getY(), 4, Color.RED);
        // }
    }

    @Override
    public void onUpdate(double dt) {
        super.onUpdate(dt);
    }
    
}
