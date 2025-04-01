package sk.piskotka.components;

import javafx.scene.paint.Color;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Renderer;

public class HealthComponent extends Component{
    private float health;
    public float getHealth() {return health;}
    private float maxHealth;

    public HealthComponent(PhysicsBody pBody, float health, float maxHealth){
        super(pBody);
        if (health > maxHealth || maxHealth <= 0 || health < 0)
            Logger.logError(getClass(), "Component initiated with invalid data");
        
        this.health = health;
        this.maxHealth = maxHealth;
    }
    public float getPercentage(){return health/maxHealth;}

    public boolean isDead(){return health <= 0;}

    public void reduceHealth(float amount){health -= amount;}
    public void increaseHealth(float amount){health = Math.min(maxHealth, health+amount);}

    public void drawHealth(Renderer ctx, Vec2 position){
        ctx.drawProgressbar(position.add(Vec2.UP().multiply(50)), 50, getPercentage(), Color.WHITESMOKE, Color.GREEN);
    }
    
}
