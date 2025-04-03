package sk.piskotka.camera;

import sk.piskotka.physics.Vec2;
import sk.piskotka.shapes.Shape;

/**
 * Abstract class representing a camera in a 2D space.
 * The camera can apply transformations such as scaling and translation
 * to shapes and points based on its position and zoom level.
 */
@SuppressWarnings("unused")
public abstract class Camera {
    protected Vec2 position;
    protected double zoom;

    /**
     * Gets the current zoom level of the camera.
     * 
     * @return the zoom level
     */
    public double getZoom() { return zoom; }

    /**
     * Sets the zoom level of the camera.
     * 
     * @param zoom the new zoom level
     */
    public void setZoom(double zoom) { this.zoom = zoom; }

    /**
     * Gets the position of the camera, adjusted by the zoom level.
     * 
     * @return the adjusted position as a {@link Vec2}
     */
    public Vec2 getPosition() { return position.multiply(zoom); }

    /**
     * Constructs a Camera with the specified position.
     * 
     * @param position the initial position of the camera
     */
    protected Camera(Vec2 position) {
        this.position = position;
        this.zoom = 1;
    }

    /**
     * Applies the camera's transformations (scaling and translation) to a shape.
     * 
     * @param shape the shape to transform
     * @return the transformed shape
     */
    public Shape applyCamera(Shape shape) {
        return shape.scaled(zoom).moved(getPosition().multiply(-1));
    }

    /**
     * Applies the camera's transformations (scaling and translation) to a point.
     * 
     * @param point the point to transform
     * @return the transformed point as a {@link Vec2}
     */
    public Vec2 applyCamera(Vec2 point) {
        return point.multiply(zoom).add(getPosition().multiply(-1));
    }

    /**
     * Updates the camera's state. This method can be overridden by subclasses
     * to implement specific update logic.
     * 
     * @param dt the time delta since the last update
     */
    public void update(double dt) {}
}
