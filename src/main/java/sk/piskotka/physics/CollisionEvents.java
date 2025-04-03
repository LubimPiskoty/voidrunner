package sk.piskotka.physics;

import sk.piskotka.components.Collider;

/**
 * Interface defining collision event handlers for objects with colliders.
 */
public interface CollisionEvents {

    /**
     * Called when a collision occurs with another collider.
     *
     * @param other the other collider involved in the collision
     */
    public void onCollision(Collider other);

    /**
     * Called when another collider enters the trigger zone.
     *
     * @param other the other collider entering the trigger zone
     */
    public void onTriggerEnter(Collider other);

    /**
     * Called when another collider exits the trigger zone.
     *
     * @param other the other collider exiting the trigger zone
     */
    public void onTriggerExit(Collider other);
}
