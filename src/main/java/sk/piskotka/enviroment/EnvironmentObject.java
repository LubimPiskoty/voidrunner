package sk.piskotka.enviroment;

import sk.piskotka.GameManager;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;

public abstract class EnvironmentObject extends PhysicsBody{
    
    public EnvironmentObject(int x, int y){
        super(x, y, GameManager.getInstance().getRandomGenerator().nextDouble(Math.PI*2));
    }

    public EnvironmentObject randomized() {
        setVelocity(Vec2.randomUnit());
        this.speed = GameManager.getInstance().getRandomGenerator().nextFloat()*20+15;
        this.setRotation(GameManager.getInstance().getRandomGenerator().nextDouble());
        setVelocity(getVelocity().multiply(speed));
        return this;
    }
}
