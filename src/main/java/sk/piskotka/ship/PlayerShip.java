package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Renderer;
import sk.piskotka.render.Shape;

public class PlayerShip extends Spaceship{

    public PlayerShip() {
        super(1000);
        shape = Shape.CreateNGon(0, 0, 50, 3);
    }


    @Override
    public void draw(Renderer ctx) {
        ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), shape.rotatedShape(rotation).getPoints(),Color.BLUE);
        // for(Vec2 p : shape.getPoints()){
        //     ctx.drawCross(pos.getX()+p.getX(), pos.getY()+p.getY(), 4, Color.RED);
        // }
    }
    
}
