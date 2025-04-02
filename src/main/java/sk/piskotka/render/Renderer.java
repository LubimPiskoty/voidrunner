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
        canvas.setCache(true);
        ctx.setGlobalBlendMode(BlendMode.SRC_OVER);
        // canvas.setEffect(new Bloom(0.1)); //! Soooo sloooow idk why
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
        ctx.strokeRoundRect(position.getX()-length/2, position.getY(), length, length/10, 5, 5);
        ctx.setFill(foreground);
        double progress = length*percentage;
        ctx.fillRoundRect(position.getX()-length/2+1, position.getY()+1, progress, length/10-2, 4, 4);
    }
    public void drawParticle(Particle p, Color color, double lifePercentage) {
        
        //Ease in expo
        lifePercentage = (Math.pow(2, 8*lifePercentage) - 1) / 255;

        Vec2 position = activeCamera.applyCamera(p.getPos());
        //TODO: Add easing function
        Color newColor = color.interpolate(Color.TRANSPARENT, lifePercentage);
        double size = 5 * (1-lifePercentage) + 1;
        size *= activeCamera.getZoom();
        ctx.setFill(newColor);
        ctx.fillOval(position.getX()-size/2, position.getY()-size/2, size, size);
        // ctx.getPixelWriter().setColor((int)position.getX(), (int)position.getY(), newColor);
        //! Why does this way of drawing ignore transparency?
    }

    public void drawArrow(Vec2 position, Vec2 vector, Color color){
        position = activeCamera.applyCamera(position);
        vector = vector.multiply(activeCamera.getZoom());
        ctx.setStroke(color);
        ctx.setFill(color);
        ctx.strokeLine(position.getX(), position.getY(), position.getX()+vector.getX(), position.getY()+vector.getY());
        double vLen = vector.length();
        double arrowSize = vLen/5;
        Vec2 endPoint = position.add(vector.multiply((vLen-arrowSize)/vLen));
        Shape triangle = new TriangleShape(0, 0, arrowSize).rotated(vector.getHeading()).moved(endPoint);
        ctx.fillPolygon(triangle.getPointsX(), triangle.getPointsY(), triangle.getSize());
    }
}
