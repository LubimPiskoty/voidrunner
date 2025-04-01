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

@SuppressWarnings("unused")
public class Renderer {
    private int width, height;
    public int getHeight() {return height;}
    public int getWidth() {return width;}

    private GraphicsContext ctx;
    public GraphicsContext getContext() {return ctx;}

    private Camera activeCamera;
    
    public Camera getActiveCamera() {return activeCamera;}
    public void setActiveCamera(Camera activeCamera) {this.activeCamera = activeCamera;}
    
    public Renderer(Canvas canvas, int width, int height) {
        this.width = width;
        this.height = height;
        // set up pixel buffer
        ctx = canvas.getGraphicsContext2D();
        ctx.setGlobalBlendMode(BlendMode.SRC_OVER);
    }

    public void clearBackground(Color color){
        ctx.setFill(color);
        ctx.fillRect(0, 0, width, height);
    }

    private Shape applyTransfrom(Shape shape, Transform transform){
        return shape.rotated(transform.getRotation()).moved(transform.getGlobalPos());
    }

    private Vec2 applyTransfrom(Vec2 point, Transform transform){
        return point.rotated(transform.getRotation()).add(transform.getGlobalPos());
    }

    /**
     * @param transform Objects tranform
     * @param shape Shape to be drawn
     * @param color Color of the drawn shape
     */
    public void drawShape(Transform transform, Shape shape, Color color){
        shape = activeCamera.applyCamera(applyTransfrom(shape, transform));
        // Logger.logDebug(getClass(), "Shape position is: " + shape.getPoints()); 
        ctx.setStroke(color);
        ctx.strokePolygon(shape.getPointsX(),shape.getPointsY(), shape.getSize());
    }

    public void drawProgressbar(Vec2 position, double length, float percentage, Color background, Color foreground) {
        position = activeCamera.applyCamera(position);
        ctx.setStroke(background);
        ctx.strokeLine(position.getX()-length/2, position.getY(), position.getX()+length/2, position.getY());
        ctx.setStroke(foreground);
        double progress = (length*percentage)-length;
        ctx.strokeLine(position.getX()-length/2, position.getY(), position.getX()+progress, position.getY());
    }
    public void drawParticle(Particle p, Color color, double lifePercentage) {
        Vec2 position = activeCamera.applyCamera(p.getPos());
        //TODO: Add easing function
        Color newColor = color.deriveColor(0, 1, 1, 1-lifePercentage);
        // Logger.logDebug(getClass(), "New color: " + newColor);
        ctx.setStroke(newColor);
        ctx.strokeRect(position.getX(), position.getY(), 1, 1);
        // ctx.getPixelWriter().setColor((int)position.getX(), (int)position.getY(), newColor);
        //! Why does this way of drawing ignore transparency?
    }
}
