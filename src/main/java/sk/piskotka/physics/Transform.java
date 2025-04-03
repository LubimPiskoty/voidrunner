package sk.piskotka.physics;

import java.util.ArrayList;
import java.util.List;

import sk.piskotka.GameManager;
import sk.piskotka.logger.Logger;

/**
 * Represents a transform in a 2D space, including position, rotation, and hierarchical relationships.
 * Allows for local and global transformations and supports parent-child relationships.
 */
public class Transform {
    private Transform parent;
    private List<Transform> children;

    private Vec2 position;
    private double rotation;

    private Transform(){
        children = new ArrayList<>();
    }
    
    /**
     * Creates a Transform with a specified local position.
     * @param position The local position of the Transform.
     */
    public Transform(Vec2 position){
        children = new ArrayList<>();
        setLocalPos(position);
    }
    
    /**
     * Creates a Transform with a specified local position and parent.
     * @param position The local position of the Transform.
     * @param parent The parent Transform.
     */
    public Transform(Vec2 position, Transform parent){
        children = new ArrayList<>();
        setLocalPos(position);
        setParent(parent);
    }

    /**
     * Sets the parent of this Transform. Updates the parent-child relationship.
     * @param parent The new parent Transform.
     */
    public void setParent(Transform parent){
        if (!isRoot())
            this.parent.removeChild(this);

        if (parent == null)
            Logger.throwError(getClass(), "Transform was created with null parent which cannot happen!! Set a valid parent");
        this.parent = parent;
        parent.addChild(this);
    }

    /**
     * Gets the parent of this Transform.
     * @return The parent Transform.
     */
    public Transform getParent(){
        return parent;
    }

    /**
     * Gets the children of this Transform.
     * @return A list of child Transforms.
     */
    public List<Transform> getChildren(){
        return children;
    }

    /**
     * Adds a child Transform to this Transform.
     * @param transform The child Transform to add.
     */
    public void addChild(Transform transform){
        children.add(transform);
    }

    /**
     * Gets a child Transform by its index.
     * @param index The index of the child Transform.
     * @return The child Transform at the specified index.
     */
    public Transform getChild(int index){
        return children.get(index);
    }

    /**
     * Removes a child Transform from this Transform.
     * @param transform The child Transform to remove.
     */
    public void removeChild(Transform transform){
        children.remove(transform); // Not sure if java can garbage collect its children or not
        //TODO: Possible memory leak
    }

    /**
     * Gets the forward direction vector based on the current rotation.
     * @return A Vec2 representing the forward direction.
     */
    public Vec2 forward(){
        return Vec2.fromHeading(getRotation());
    }

    /**
     * Gets the local position of this Transform.
     * @return The local position as a Vec2.
     */
    public Vec2 getLocalPos() {
        return position;
    }

    /**
     * Sets the local position of this Transform.
     * @param position The new local position as a Vec2.
     */
    public void setLocalPos(Vec2 position) {
        this.position = position;
    }

    /**
     * Gets the global rotation of this Transform.
     * @return The global rotation in degrees.
     */
    public double getRotation(){
        if (parent.isRoot())
            return rotation;
        else
            return rotation + parent.getRotation();
    }

    /**
     * Sets the local rotation of this Transform.
     * @param rotation The new local rotation in degrees.
     */
    public void setRotation(double rotation){
        this.rotation = rotation;
    }

    /**
     * Gets the global position of this Transform.
     * @return The global position as a Vec2.
     */
    public Vec2 getGlobalPos(){
        if (isRoot() || parent.isRoot())
            return getLocalPos(); // Ill just ignore root rotation and position

        return getLocalPos().rotated(parent.getRotation()).add(parent.getGlobalPos());
    }

    /**
     * Converts a local position to a global position.
     * @param local The local position as a Vec2.
     * @return The global position as a Vec2.
     */
    public Vec2 getGlobalFromLocalPos(Vec2 local){
        return local.rotated(getRotation()).add(getGlobalPos());
    }

    /**
     * Transforms a list of local points to global points.
     * @param local A list of local points as Vec2 objects.
     * @return A list of global points as Vec2 objects.
     */
    public List<Vec2> transformPoints(List<Vec2> local){
        Vec2 globalPos = getGlobalPos();
        List<Vec2> global = new ArrayList<>();
        for (Vec2 point : local) {
            global.add(point.rotated(getRotation()).add(globalPos));
        }
        return global;
    }

    /**
     * Checks if this Transform is the root (has no parent).
     * @return True if this Transform is the root, false otherwise.
     */
    public boolean isRoot(){
        return parent == null;
    }

    /**
     * Creates a root Transform with no parent.
     * @return A new root Transform.
     */
    public static Transform createRoot(){
        return new Transform();
    }

    /**
     * Returns a string representation of this Transform.
     * @return A string representation of the Transform.
     */
    @Override
    public String toString() {
        String name = super.toString();
        return name.substring(name.lastIndexOf(".")+1);
    }

    /**
     * Handles the death of this Transform by removing it from its parent's children.
     */
    public void onDeath(){
        getParent().removeChild(this);
    };

    /**
     * Updates this Transform. Can be overridden for custom behavior.
     * @param dt The delta time since the last update.
     */
    public void update(double dt){};

    /**
     * Creates a new Transform in the current level.
     * @param transform The Transform to create.
     */
    public static void Create(Transform transform){
        if (GameManager.getLevel() == null)
            Logger.logError(Transform.class, "Transform is not in level and therefore cannot create objects");
        else
            GameManager.getLevel().create(transform);
        }
        
    /**
     * Destroys a Transform in the current level.
     * @param transform The Transform to destroy.
     */
    public static void Destroy(Transform transform){
        if (GameManager.getLevel() == null)
            Logger.logError(Transform.class, "Transform is not in level and therefore cannot destroy objects");
        else
            GameManager.getLevel().destroy(transform);
    }
}
