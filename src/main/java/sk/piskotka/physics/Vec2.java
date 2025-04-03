package sk.piskotka.physics;

/**
 * Represents a 2D vector with double precision.
 * Provides various vector operations such as addition, subtraction, scaling, normalization, and rotation.
 */
public final class Vec2 {
    private double x, y;

    /**
     * Gets the x-coordinate of the vector.
     * @return the x-coordinate.
     */
    public double getX() { return x; }

    /**
     * Gets the y-coordinate of the vector.
     * @return the y-coordinate.
     */
    public double getY() { return y; }

    /**
     * Constructs a new vector with the specified x and y components.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new vector by copying another vector.
     * @param other the vector to copy.
     */
    public Vec2(Vec2 other) {
        this(other.x, other.y);
    }

    /**
     * Adds another vector to this vector.
     * @param other the vector to add.
     * @return a new vector representing the sum.
     */
    public Vec2 add(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    /**
     * Multiplies this vector by a scalar.
     * @param scalar the scalar value.
     * @return a new vector representing the scaled vector.
     */
    public Vec2 multiply(double scalar) {
        return new Vec2(this.x * scalar, this.y * scalar);
    }

    /**
     * Subtracts another vector from this vector.
     * @param other the vector to subtract.
     * @return a new vector representing the difference.
     */
    public Vec2 subtract(Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    /**
     * Calculates the length (magnitude) of the vector.
     * @return the length of the vector.
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Normalizes the vector to have a length of 1.
     * @return a new normalized vector, or a zero vector if the length is near zero.
     */
    public Vec2 normalized() {
        double l = length();
        return (Math.abs(l) < 0.000001) ? new Vec2(0, 0) : new Vec2(x / l, y / l);
    }

    /**
     * Rotates the vector counter-clockwise by the specified angle in radians.
     * @param angle the angle in radians.
     * @return a new rotated vector.
     */
    public Vec2 rotated(double angle) {
        double currAngle = Math.atan2(y, x);
        double r = length();
        return new Vec2(Math.cos(currAngle + angle) * r, Math.sin(currAngle + angle) * r);
    }

    /**
     * Sets the x and y components of this vector.
     * @param x the new x-coordinate.
     * @param y the new y-coordinate.
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the components of this vector to match another vector.
     * @param v the vector to copy.
     */
    public void set(Vec2 v) {
        set(v.x, v.y);
    }

    /**
     * Gets the heading (angle) of the vector in radians.
     * @return the angle in radians.
     */
    public double getHeading() {
        return Math.atan2(y, x);
    }

    /**
     * Calculates the dot product of this vector and another vector.
     * @param other the other vector.
     * @return the dot product.
     */
    public double dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    /**
     * Gets a vector normal to this vector.
     * @return a new vector that is normal to this vector.
     */
    public Vec2 normal() {
        return new Vec2(-y, x);
    }

    /**
     * Linearly interpolates between this vector and a target vector.
     * @param target the target vector.
     * @param t the interpolation factor (0.0 to 1.0).
     * @return a new interpolated vector.
     */
    public Vec2 lerp(Vec2 target, double t) {
        return new Vec2(
            this.x + (target.x - this.x) * t,
            this.y + (target.y - this.y) * t
        );
    }

    /**
     * Returns a vector with both components set to zero.
     * @return a zero vector.
     */
    public static Vec2 ZERO() { return new Vec2(0, 0); }

    /**
     * Returns a vector with both components set to one.
     * @return a vector with components (1, 1).
     */
    public static Vec2 ONES() { return new Vec2(1, 1); }

    /**
     * Returns a unit vector pointing up (0, 1).
     * @return a unit vector pointing up.
     */
    public static Vec2 UP() { return new Vec2(0, 1); }

    /**
     * Returns a unit vector pointing down (0, -1).
     * @return a unit vector pointing down.
     */
    public static Vec2 DOWN() { return new Vec2(0, -1); }

    /**
     * Returns a unit vector pointing left (-1, 0).
     * @return a unit vector pointing left.
     */
    public static Vec2 LEFT() { return new Vec2(-1, 0); }

    /**
     * Returns a unit vector pointing right (1, 0).
     * @return a unit vector pointing right.
     */
    public static Vec2 RIGHT() { return new Vec2(1, 0); }

    /**
     * Generates a random unit vector.
     * @return a random unit vector.
     */
    public static Vec2 randomUnit() {
        Vec2 v = new Vec2(Math.random() - 0.5, Math.random() - 0.5);
        return v.normalized();
    }

    /**
     * Creates a vector from a given heading (angle in radians).
     * @param angle the angle in radians.
     * @return a new vector pointing in the direction of the angle.
     */
    public static Vec2 fromHeading(double angle) {
        return new Vec2(Math.cos(angle), Math.sin(angle));
    }

    /**
     * Returns a string representation of the vector in the format "(x, y)".
     * @return a string representation of the vector.
     */
    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }

    /**
     * Checks if this vector is equal to another object.
     * @param obj the object to compare.
     * @return true if the object is a Vec2 with the same components, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vec2 other) {
            return other.x == x && other.y == y;
        }
        return false;
    }

    /**
     * Generates a hash code for this vector.
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }
}
