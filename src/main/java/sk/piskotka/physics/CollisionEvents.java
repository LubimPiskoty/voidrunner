package sk.piskotka.physics;

import sk.piskotka.components.Collider;

public interface CollisionEvents {
    public void onCollision(Collider other);
    public void onTriggerEnter(Collider other);
    public void onTriggerExit(Collider other);
}
