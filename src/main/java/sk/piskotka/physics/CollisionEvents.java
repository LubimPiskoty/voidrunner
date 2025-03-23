package sk.piskotka.physics;

public interface CollisionEvents {
    public void onCollision(Collider other);
    public void onTriggerEnter(Collider other);
    public void onTriggerExit(Collider other);
}
