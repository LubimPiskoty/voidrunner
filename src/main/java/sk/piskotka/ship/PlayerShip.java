package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.NormalAmmo;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;
import sk.piskotka.shapes.Shape;

public class PlayerShip extends Spaceship{
    private Shape nose;
    private Transform gunTransform;

    public PlayerShip(int x, int y, float health, float maxHealth) {
        super(x, y, 1000, 5, health, maxHealth);
        setShape(new PolygonShape(0, 0, 50, 3));
        nose = new PolygonShape(0, 0, 9, 4);
        
        gunTransform = new Transform(new Vec2(50, 0), this);
    }

    @Override
    public void shoot() {
        Vec2 gunPos = gunTransform.getGlobalPos(); 
        Create(new NormalAmmo(this, gunPos.getX(), gunPos.getY(), getRotation()));
    }

    @Override
    public void draw(Renderer ctx) {
        ctx.drawShape(this, getShape(), Color.BLUE);
        ctx.drawShape(gunTransform, nose, Color.BLUEVIOLET);
        health.drawHealth(ctx, getGlobalPos());
    }
}
