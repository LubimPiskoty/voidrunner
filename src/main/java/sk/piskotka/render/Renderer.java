package sk.piskotka.render;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import sk.piskotka.camera.Camera;
import sk.piskotka.effects.Particle;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.shapes.Shape;
import sk.piskotka.shapes.TriangleShape;

/**
 * The {@code Renderer} class is responsible for rendering objects on the canvas.
 * It handles the drawing of shapes, particles, progress bars, and other graphical elements 
 * using the JavaFX {@link GraphicsContext}.
 * <p>
 * It supports transformations such as rotation, translation, and camera application 
 * to render objects correctly based on their position and orientation in the game world.
 * </p>
 * 
 * @author Piskotka
 */
public class Renderer {
    private final int width;
    private final int height;
    private final GraphicsContext ctx;
    private Camera activeCamera;
    
    /**
     * Constructs a new {@code Renderer} instance with the specified canvas and dimensions.
     * 
     * @param canvas The canvas on which the rendering will occur.
     * @param width The width of the canvas.
     * @param height The height of the canvas.
     */
    public Renderer(Canvas canvas, int width, int height) {
        this.width = width;
        this.height = height;
        ctx = canvas.getGraphicsContext2D();
        canvas.setCache(true);
        ctx.setGlobalBlendMode(BlendMode.SRC_OVER);
    }

    /**
     * Clears the background of the canvas with the specified color.
     * 
     * @param color The color to fill the background with.
     */
    public void clearBackground(Color color){
        ctx.setFill(color);
        ctx.fillRect(0, 0, width, height);
    }

    /**
     * Applies the given transformation to a shape (rotation and translation).
     * 
     * @param shape The shape to transform.
     * @param transform The transformation to apply.
     * @return A new shape transformed by the given transform.
     */
    private Shape applyTransfrom(Shape shape, Transform transform){
        return shape.rotated(transform.getRotation()).moved(transform.getGlobalPos());
    }

    /**
     * Applies the given transformation to a point (rotation and translation).
     * 
     * @param point The point to transform.
     * @param transform The transformation to apply.
     * @return The transformed point.
     */
    private Vec2 applyTransfrom(Vec2 point, Transform transform){
        return point.rotated(transform.getRotation()).add(transform.getGlobalPos());
    }

    /**
     * Draws a shape on the canvas using the given transformation and color.
     * 
     * @param transform The transformation to apply to the shape.
     * @param shape The shape to draw.
     * @param color The color to use for the shape's outline.
     */
    public void drawShape(Transform transform, Shape shape, Color color){
        shape = activeCamera.applyCamera(applyTransfrom(shape, transform));
        ctx.setStroke(color);
        ctx.strokePolygon(shape.getPointsX(), shape.getPointsY(), shape.getSize());
    }

    /**
     * Draws a progress bar on the canvas at the given position with a specified length,
     * percentage, and colors for the background and foreground.
     * 
     * @param position The position of the progress bar on the canvas.
     * @param length The length of the progress bar.
     * @param percentage The current progress (from 0.0 to 1.0).
     * @param background The background color of the progress bar.
     * @param foreground The foreground color of the progress bar.
     */
    public void drawProgressbar(Vec2 position, double length, float percentage, Color background, Color foreground) {
        position = activeCamera.applyCamera(position);
        ctx.setStroke(background);
        ctx.strokeRoundRect(position.getX()-length/2, position.getY(), length, length/10, 5, 5);
        ctx.setFill(foreground);
        double progress = length * percentage;
        ctx.fillRoundRect(position.getX()-length/2+1, position.getY()+1, progress, length/10-2, 4, 4);
    }

    /**
     * Draws a particle on the canvas at the given position, using a color that interpolates 
     * from the specified color to transparent based on the particle's life percentage.
     * 
     * @param p The particle to draw.
     * @param color The color of the particle at the start of its life.
     * @param lifePercentage The percentage of the particle's life (from 0.0 to 1.0).
     */
    public void drawParticle(Particle p, Color color) {
        double lifePercentage = (Math.pow(2, 8*p.getLifePercentage()) - 1) / 255;

        Vec2 position = activeCamera.applyCamera(p.getPos());
        Color newColor = color.interpolate(Color.TRANSPARENT, lifePercentage);
        double size = 5 * (1-lifePercentage) + 1;
        size *= activeCamera.getZoom();
        ctx.setFill(newColor);
        ctx.fillOval(position.getX()-size/2, position.getY()-size/2, size, size);
    }

    /**
     * Draws an arrow at the specified position, pointing in the direction of the given vector.
     * The arrow is drawn with the specified color.
     * 
     * @param position The position where the arrow's tail should be located.
     * @param vector The vector representing the direction and length of the arrow.
     * @param color The color of the arrow.
     */
    public void drawArrow(Vec2 position, Vec2 vector, Color color){
        position = activeCamera.applyCamera(position);
        vector = vector.multiply(activeCamera.getZoom());
        ctx.setStroke(color);
        ctx.setFill(color);
        ctx.strokeLine(position.getX(), position.getY(), position.getX() + vector.getX(), position.getY() + vector.getY());
        double vLen = vector.length();
        double arrowSize = vLen / 5;
        Vec2 endPoint = position.add(vector.multiply((vLen - arrowSize) / vLen));
        Shape triangle = new TriangleShape(0, 0, arrowSize).rotated(vector.getHeading()).moved(endPoint);
        ctx.fillPolygon(triangle.getPointsX(), triangle.getPointsY(), triangle.getSize());
    }

    public Camera getActiveCamera() {
        return activeCamera;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setActiveCamera(Camera activeCamera) {
        this.activeCamera = activeCamera;
    }
}
