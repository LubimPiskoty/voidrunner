package sk.piskotka.enviroment;

import javafx.scene.paint.Color;
import sk.piskotka.render.Renderer;
import sk.piskotka.render.shapes.RectangleShape;

public class Asteroid extends EnvironmentObject {
    private double rotationSpeed;
    public Asteroid(int x, int y, double rotationSpeed) {
        super(x, y);
        this.rotationSpeed = rotationSpeed;
        this.shape = new RectangleShape(-50, -20, 100, 70).create();
    }

    @Override
    public void draw(Renderer ctx) {
        ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), this.shape.rotatedShape(rotation).getPoints(), Color.BURLYWOOD);
    }

    @Override
    public void onUpdate(double dt) {
        rotation += rotationSpeed*dt;
        if (rotation > Math.PI*2)
            rotation -= Math.PI*2;
    }

}
