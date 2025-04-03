package sk.piskotka.shapes;

/**
 * The {@code RectangleShape} class represents a rectangular shape defined by four points.
 * <p>
 * This shape is initialized with a starting point (x, y) and the width (w) and height (h) of the rectangle. 
 * It automatically generates the four corner points of the rectangle.
 * </p>
 * 
 * @author Piskotka
 */
public class RectangleShape extends Shape {

    /**
     * Constructs a new rectangle shape with the specified position, width, and height.
     * <p>
     * The rectangle is defined by its top-left corner at (x, y) and its width and height (w, h).
     * </p>
     * 
     * @param x The x-coordinate of the top-left corner of the rectangle.
     * @param y The y-coordinate of the top-left corner of the rectangle.
     * @param w The width of the rectangle.
     * @param h The height of the rectangle.
     */
    public RectangleShape(int x, int y, int w, int h) {
        super(4);
        addPoint(x, y);
        addPoint(x + w, y);
        addPoint(x + w, y + h);
        addPoint(x, y + h);
    }

}
