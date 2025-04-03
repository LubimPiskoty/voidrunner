package sk.piskotka.shapes;

/**
 * The {@code TriangleShape} class represents a shape that is a triangle.
 * <p>
 * This class extends the {@link PolygonShape} class and constructs a triangle shape
 * by providing the number of vertices (3) and the size of the triangle.
 * </p>
 * <p>
 * The {@code TriangleShape} class is useful for representing triangular objects 
 * in the game world.
 * </p>
 * 
 * @author Piskotka
 */
public class TriangleShape extends PolygonShape {

    /**
     * Constructs a new {@code TriangleShape} at the specified position with the given size.
     * 
     * @param x The x-coordinate of the triangle's position.
     * @param y The y-coordinate of the triangle's position.
     * @param arrowSize The size of the triangle. This value will influence the overall size of the shape.
     */
    public TriangleShape(double x, double y, double arrowSize) {
        super(x, y, arrowSize, 3);  // Calls the constructor of PolygonShape with 3 vertices for a triangle
    }
}
