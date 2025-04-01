package sk.piskotka.enviroment;

import javafx.scene.paint.Color;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;

public class Asteroid extends EnvironmentObject {
    private double rotationSpeed;
    public Asteroid(int x, int y, double rotationSpeed) {
        super(x, y);
        this.rotationSpeed = rotationSpeed;
        // this.setShape(new RectangleShape(-50, -20, 100, 70));
        this.setShape(new PolygonShape(0, 0, 100, 4));
    }

    @Override
    public void draw(Renderer ctx) {
        ctx.drawShape(this, getShape(), Color.BURLYWOOD);
    }

    @Override
    public void update(double dt) {
        setRotation(getRotation() + rotationSpeed*dt);
        if (getRotation() > Math.PI*2)
            setRotation(getRotation() - Math.PI*2);

        super.update(dt);
    }

}
