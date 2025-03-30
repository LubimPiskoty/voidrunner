package sk.piskotka.ship;

import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.Projectile;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;
import sk.piskotka.shapes.TriangleShape;

public class EnemyShip extends Spaceship {
    private Transform[] gunPoints;

    public EnemyShip(int x, int y, float speed, float attackSpeed) {
        super(x, y, speed, attackSpeed);
        gunPoints = new Transform[5];
        setShape(new PolygonShape(0, 0, 50, 5));
        // A special gunpoint for each vertecie
        int i = 0;
        for(Vec2 verticie : getShape().getPoints()){
            gunPoints[i] = new Transform(verticie, this);
            gunPoints[i].setLocalRot(verticie.getHeading());
            i++;
        }
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        attemptToShoot();
        setLocalRot(getLocalRot()+speed*dt);
    }

    @Override
    public void draw(Renderer ctx) {
        ctx.drawPolygonWithTransform(this, getShape(), Color.RED);
        for(Transform gunPoint : gunPoints)
            ctx.drawCross(gunPoint.getGlobalPos(), 5, Color.CRIMSON);
    }
    
    @Override
    protected void shoot() {
        for(Transform gunPoint : gunPoints){
            Vec2 gunPos = gunPoint.getGlobalPos();
            Projectile p = new Projectile(gunPos.getX(), gunPos.getY(), 1000, gunPoint.getGlobalRot());
            GameManager.getLevel().create(p);
        }
        
    }
    
}
