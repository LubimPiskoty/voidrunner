package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.NormalAmmo;
import sk.piskotka.projectile.Projectile;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;

public class TankEnemy extends EnemyShip {
    private Transform[] gunPoints;

    public TankEnemy(int x, int y) {
        super(x, y, 0.6f, 0.5f, 15, 15);
        gunPoints = new Transform[5];
        setShape(new PolygonShape(0, 0, 50, 5));
        // A special gunpoint for each vertecie
        int i = 0;
        for(Vec2 verticie : getShape().getPoints()){
            gunPoints[i] = new Transform(verticie, this);
            gunPoints[i].setRotation(verticie.getHeading());
            i++;
        };
    }

    @Override
    protected void shoot() {
        for(Transform gunPoint : gunPoints){
            Vec2 gunPos = gunPoint.getGlobalPos();
            Projectile p = new NormalAmmo(this, gunPos.getX(), gunPos.getY(), gunPoint.getRotation());
            GameManager.getLevel().create(p);
        }   
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        setRotation(getRotation()+speed*dt);
    }

    @Override
    public void draw(Renderer ctx) {
        super.draw(ctx);
        ctx.drawShape(this, getShape(), Color.DARKGREEN);
        for(Transform gunPoint : gunPoints)
            ctx.drawArrow(gunPoint.getGlobalPos(), Vec2.fromHeading(gunPoint.getRotation()).multiply(20), Color.GREEN);

    }
}
