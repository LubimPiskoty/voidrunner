package sk.piskotka.effects;

import java.util.ArrayList;
import java.util.List;

import sk.piskotka.guns.Timer;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Drawable;

public abstract class Effect extends Transform implements Drawable{
    protected List<Particle> pointCloud;
    protected Timer deathTimer;

    public Effect(Vec2 position, double duration){
        super(position);
        pointCloud = new ArrayList<>();
        deathTimer = new Timer(duration);
    }

    public void update(double dt){
        if (deathTimer.isReady())
            Destroy(this);
        else{
            deathTimer.tick(dt);
            for (Particle particle : pointCloud)
                particle.update(dt);
        }
    }
}
