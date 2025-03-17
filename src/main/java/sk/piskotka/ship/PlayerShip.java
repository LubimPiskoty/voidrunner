package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.Physics.Vec2;
import sk.piskotka.render.Renderer;

public class PlayerShip extends Spaceship{

    public PlayerShip() {
        super(1000);
    }
    
    @Override
    public void draw(Renderer ctx) {
        ctx.drawCross((int)pos.getX(), (int)pos.getY(), 10, Color.RED);
    }
    
}
