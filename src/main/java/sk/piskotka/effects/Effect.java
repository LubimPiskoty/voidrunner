package sk.piskotka.effects;

import java.util.ArrayList;
import java.util.List;

import sk.piskotka.guns.Timer;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Drawable;

/**
 * Represents an abstract effect in the game, which is drawable and has a position.
 * The effect consists of a collection of particles and has a limited lifespan.
 */
public abstract class Effect extends Transform implements Drawable {
    protected List<Particle> pointCloud;
    protected Timer deathTimer;

    /**
     * Constructs an Effect with a specified position and duration.
     *
     * @param position The initial position of the effect.
     * @param duration The duration of the effect in seconds.
     */
    public Effect(Vec2 position, double duration) {
        super(position);
        pointCloud = new ArrayList<>();
        deathTimer = new Timer(duration);
    }

    /**
     * Updates the state of the effect. If the effect's duration has elapsed,
     * it is destroyed. Otherwise, it updates the timer and all particles.
     *
     * @param dt The time delta in seconds since the last update.
     */
    @Override
    public void update(double dt) {
        if (deathTimer.isReady())
            Destroy(this);
        else {
            deathTimer.tick(dt);
            for (Particle particle : pointCloud)
                particle.update(dt);
        }
    }
}
