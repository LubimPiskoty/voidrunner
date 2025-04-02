package sk.piskotka.effects;

import javafx.scene.paint.Color;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Renderer;

public class SparksEffect extends Effect{
    private Color color;

    public SparksEffect(Vec2 position, Color color, double duration, int effectStrength){
        super(position, duration);
        this.color = color;
        int count = (int)Math.pow(effectStrength, 2)*10;
        for(int i = 0; i < count; i++){
            double strength = Math.random();
            pointCloud.add(new Particle(position, Vec2.randomUnit().multiply(strength*effectStrength*50+effectStrength*10+10)));
        }
    }

    @Override
    public void draw(Renderer ctx) {
        for(Particle p : pointCloud)
            ctx.drawParticle(p, color, deathTimer.completion());
    }
    
}
