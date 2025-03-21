package sk.piskotka.enviroment;

import javafx.scene.paint.Color;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.RectangleShape;

public class Asteroid extends EnvironmentObject {
    private double rotationSpeed;
    public Asteroid(int x, int y, double rotationSpeed) {
        super(x, y);
        this.rotationSpeed = rotationSpeed;
        this.setShape(new RectangleShape(-50, -20, 100, 70));
    }

    @Override
    public void draw(Renderer ctx) {
        ctx.drawPolygonWithOffset(getLocalPos().getX(), getLocalPos().getY(),
                                getShape().rotated(getLocalRot()).getPoints(), Color.BURLYWOOD);
    }

    @Override
    public void update(double dt) {
        setLocalRot(getLocalRot() + rotationSpeed*dt);
        if (getLocalRot() > Math.PI*2)
            setLocalRot(getLocalRot() - Math.PI*2);

        super.update(dt);
    }

}
