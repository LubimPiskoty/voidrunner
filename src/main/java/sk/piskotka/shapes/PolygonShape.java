package sk.piskotka.shapes;

/**
 * The {@code PolygonShape} class represents a polygon with a specified number of vertices.
 * <p>
 * This shape is initialized by specifying a center point (x, y), the size (radius) of the polygon, 
 * and the number of vertices. The vertices are distributed evenly around the center, forming a regular polygon.
 * </p>
 * 
 * @author Piskotka
 */
public class PolygonShape extends Shape {

    /**
     * Constructs a new polygon shape with the specified center, size, and vertex count.
     * <p>
     * The polygon is created by calculating the positions of its vertices, evenly spaced in a circular pattern.
     * </p>
     * 
     * @param x The x-coordinate of the center of the polygon.
     * @param y The y-coordinate of the center of the polygon.
     * @param arrowSize The radius (size) of the polygon, defining the distance of each vertex from the center.
     * @param vertexCount The number of vertices of the polygon. 
     *                    The higher the count, the closer the shape will resemble a circle.
     */
    public PolygonShape(double x, double y, double arrowSize, int vertexCount) {
        super(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            double angle = Math.PI * 2 * ((double) i / (double) vertexCount);
            addPoint((int) (x + Math.cos(angle) * arrowSize), (int) (y + Math.sin(angle) * arrowSize));
        }
    }

}
