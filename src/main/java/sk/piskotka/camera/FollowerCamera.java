package sk.piskotka.camera;

import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;

/**
 * A camera that follows a target Transform with a specified offset and follow speed.
 */
public class FollowerCamera extends Camera {
    private Vec2 offset;
    private Transform target;
    
    /**
     * Sets the target Transform for the camera to follow.
     * 
     * @param target The Transform to follow.
     */
    public void setTarget(Transform target) {
        this.target = target;
    }

    private final double followSpeed;

    /**
     * Constructs a FollowerCamera.
     * 
     * @param target The Transform to follow.
     * @param offset The offset to place the target into a specific spot in the camera.
     * @param followSpeed The speed at which the camera follows the target.
     */
    public FollowerCamera(Transform target, Vec2 offset, double followSpeed) {
        super(offset);
        this.target = target;
        this.offset = offset;
        this.followSpeed = followSpeed;
    }

    /**
     * Gets the current position of the camera, adjusted by the offset.
     * 
     * @return The position of the camera as a Vec2.
     */
    @Override
    public Vec2 getPosition() {
        return super.getPosition().subtract(offset);
    }

    /**
     * Updates the camera's position to follow the target based on the elapsed time.
     * 
     * @param dt The time delta since the last update.
     */
    @Override
    public void update(double dt) {
        position = position.lerp(target.getGlobalPos(), followSpeed * dt);
    }
}
