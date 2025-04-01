package sk.piskotka.effects;

import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Renderer;

public class SparksEffect extends Effect{
    private Color color;

    public SparksEffect(Vec2 position, Color color, double duration, int count){
        super(position, duration);
        this.color = color;
        for(int i = 0; i < count; i++){
            double strength = GameManager.getInstance().getRandomGenerator().nextDouble();
            pointCloud.add(new Particle(position, Vec2.randomUnit().multiply(strength*100+10)));
        }
    }

    @Override
    public void draw(Renderer ctx) {
        for(Particle p : pointCloud)
            ctx.drawParticle(p, color, deathTimer.completion());
    }
    
}
