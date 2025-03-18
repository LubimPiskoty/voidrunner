package sk.piskotka.projectile;

import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.render.Renderer;

public class Projectile extends PhysicsBody{

    public Projectile(int x, int y) {
        super(x, y);
    }
    
    @Override
    public void draw(Renderer ctx) {
    }

    @Override
    public void onUpdate(double dt) {
    }
    
}
