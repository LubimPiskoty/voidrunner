package sk.piskotka.enviroment;

import sk.piskotka.GameManager;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;

/**
 * Represents an abstract object in the environment that extends the PhysicsBody class.
 * Provides functionality for randomizing its properties such as velocity, speed, and rotation.
 */
public abstract class EnvironmentObject extends PhysicsBody {

    /**
     * Constructs an EnvironmentObject with the specified position and a random initial rotation.
     *
     * @param x the x-coordinate of the object
     * @param y the y-coordinate of the object
     */
    public EnvironmentObject(int x, int y) {
        super(x, y, GameManager.getInstance().getRandomGenerator().nextDouble(Math.PI * 2));
    }

    /**
     * Randomizes the properties of the EnvironmentObject, including velocity, speed, and rotation.
     *
     * @return the current instance of EnvironmentObject with randomized properties
     */
    public EnvironmentObject randomized() {
        setVelocity(Vec2.randomUnit());
        this.speed = GameManager.getInstance().getRandomGenerator().nextFloat() * 20 + 15;
        this.setRotation(GameManager.getInstance().getRandomGenerator().nextDouble());
        setVelocity(getVelocity().multiply(speed));
        return this;
    }
}
