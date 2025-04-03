package sk.piskotka.shapes;

import java.util.ArrayList;
import java.util.List;

import sk.piskotka.logger.Logger;
import sk.piskotka.physics.Vec2;

/**
 * The {@code Shape} class represents a geometric shape defined by a series of points.
 * <p>
 * This class follows the Prototype Creational Pattern and provides functionality 
 * for manipulating shapes, including rotating, moving, and scaling the shape, as well 
 * as copying and adding points.
 * </p>
 * <p>
 * The shape is defined by a set of points (X, Y) and allows for transformations on these points.
 * </p>
 * 
 * @author Piskotka
 */
public class Shape {
    
    private int capacity;  // The maximum number of points the shape can hold
    private int size;  // The current number of points in the shape

    /**
     * Gets the current number of points in the shape.
     * 
     * @return The number of points in the shape.
     */
    public int getSize() {
        return size;
    }

    private double[] pointsX;  // Array of X coordinates of the points
    private double[] pointsY;  // Array of Y coordinates of the points

    /**
     * Initializes the shape with the given capacity.
     * 
     * @param capacity The maximum number of points the shape can hold.
     */
    private void init(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        pointsX = new double[capacity];
        pointsY = new double[capacity];
    }

    /**
     * Constructs a new shape with a given capacity.
     * 
     * @param capacity The maximum number of points the shape can hold.
     */
    protected Shape(int capacity) {
        init(capacity);
    }

    /**
     * Constructs a new shape as a copy of the given shape.
     * 
     * @param copy The shape to copy.
     */
    protected Shape(Shape copy) {
        init(copy.size);
        deepCopyPoints(copy);
    }

    /**
     * Copies the points of the given shape into this shape.
     * 
     * @param copy The shape to copy the points from.
     */
    private void deepCopyPoints(Shape copy) {
        for (int i = 0; i < copy.size; i++) {
            pointsX[i] = copy.getPointsX()[i];
            pointsY[i] = copy.getPointsY()[i];
        }
        size = copy.size;
    }

    /**
     * Adds a new point to the shape.
     * 
     * @param x The X coordinate of the new point.
     * @param y The Y coordinate of the new point.
     * @throws RuntimeException if the shape cannot hold any more points.
     */
    protected void addPoint(double x, double y) {
        if (size == capacity)
            Logger.throwError(getClass(), "This shape cannot hold any more points!!");
        pointsX[size] = x;
        pointsY[size] = y;
        size++;
    }

    /**
     * Rotates the shape by a given angle.
     * 
     * @param angle The angle to rotate the shape, in radians.
     * @return A new {@code Shape} object representing the rotated shape.
     */
    public Shape rotated(double angle) {
        Shape copy = copy();
        for (int i = 0; i < size; i++) {
            Vec2 rotated = new Vec2(pointsX[i], pointsY[i]).rotated(angle);
            copy.pointsX[i] = rotated.getX();
            copy.pointsY[i] = rotated.getY();
        }
        return copy;
    }

    /**
     * Returns a list of points representing the shape.
     * 
     * @return A list of {@link Vec2} objects representing the points of the shape.
     */
    public List<Vec2> getPoints() {
        List<Vec2> points = new ArrayList<>();
        for (int i = 0; i < size; i++)
            points.add(new Vec2(pointsX[i], pointsY[i]));
        return points;
    }

    /**
     * Moves the shape by a given offset.
     * 
     * @param offset The {@link Vec2} representing the offset to move the shape.
     * @return A new {@code Shape} object representing the moved shape.
     */
    public Shape moved(Vec2 offset) {
        Shape copy = copy();
        for (int i = 0; i < size; i++) {
            Vec2 moved = new Vec2(pointsX[i], pointsY[i]).add(offset);
            copy.pointsX[i] = moved.getX();
            copy.pointsY[i] = moved.getY();
        }
        return copy;
    }

    /**
     * Scales the shape by a given factor.
     * 
     * @param factor The scaling factor.
     * @return A new {@code Shape} object representing the scaled shape.
     */
    public Shape scaled(double factor) {
        Shape copy = copy();
        for (int i = 0; i < size; i++) {
            Vec2 scaled = new Vec2(pointsX[i], pointsY[i]).multiply(factor);
            copy.pointsX[i] = scaled.getX();
            copy.pointsY[i] = scaled.getY();
        }
        return copy;
    }

    /**
     * Checks if the shape is empty (has no points).
     * 
     * @return {@code true} if the shape has no points, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Creates a copy of this shape.
     * 
     * @return A new {@code Shape} object that is a copy of this shape.
     */
    public Shape copy() {
        return new Shape(this);
    }

    public double[] getPointsX() {
        return pointsX;
    }

    public double[] getPointsY() {
        return pointsY;
    }
}
