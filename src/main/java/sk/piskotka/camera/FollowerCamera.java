package sk.piskotka.camera;

import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;

public class FollowerCamera extends Camera{
    private Vec2 offset;
    private Transform target;
    public void setTarget(Transform target) {this.target = target;}

    private double followSpeed;

    /**
     * @param target Transform to follow
     * @param offset Offset to place the target into specific spot in the camera
     * @param followSpeed Affects the speed of the camera 
     */
    public FollowerCamera(Transform target, Vec2 offset, double followSpeed) {
        super(offset);
        this.target = target;
        this.offset = offset;
        this.followSpeed = followSpeed;
    }

    @Override
    public Vec2 getPosition() {
        return super.getPosition().subtract(offset);
    }

    @Override
    public void update(double dt) {
        position = position.lerp(target.getGlobalPos(), followSpeed*dt);
    }
    
}
