package sk.piskotka.enviroment;

import sk.piskotka.GameManager;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;

public abstract class EnvironmentObject extends PhysicsBody{
    
    public EnvironmentObject(int x, int y){
        super(x, y);
    }

    public EnvironmentObject randomized() {
        this.vel = Vec2.randomUnit();
        this.speed = GameManager.getInstance().randomGenerator.nextFloat()*20+15;
        this.rotation = GameManager.getInstance().randomGenerator.nextDouble();
        this.vel.multiply(speed);
        return this;
    }
}
