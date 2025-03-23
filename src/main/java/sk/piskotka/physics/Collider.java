package sk.piskotka.physics;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.util.Pair;
import sk.piskotka.GameManager;
import sk.piskotka.logger.Logger;
import sk.piskotka.render.Drawable;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;
import sk.piskotka.shapes.RectangleShape;
import sk.piskotka.shapes.Shape;

public class Collider implements Drawable {
    private boolean isTrigger;
    private boolean isColliding;
    private Shape shape;
    private Transform transform;
    
    private double boundingCircleRadius;
    private Vec2 boundingCircleCenter;

    public Collider(Transform transform, Shape shape, boolean isTrigger){
        if (shape == null)
            Logger.throwError(getClass(), "Shape is null. Try checking object instantiation");
        
        if (transform == null)
            Logger.throwError(getClass(), "Transform is null. Try checking object instantiation");
        
        this.transform = transform;
        this.shape = shape;
        this.isTrigger = isTrigger;
        computeBoundingCircle();
    }

    private void computeBoundingCircle(){

        boundingCircleCenter = Vec2.ZERO();
        boundingCircleRadius = Double.POSITIVE_INFINITY;

        // Find the centroid
        for(Vec2 p : shape.getPoints())
            boundingCircleCenter.add(p);
        boundingCircleCenter.multiply(1/shape.getPoints().size());

        //Find the furthest points from centroid
        for(Vec2 p : shape.getPoints()){
            double dist = boundingCircleCenter.subtract(p).length();
            if (boundingCircleRadius > dist)
                boundingCircleRadius = dist;
        }
    }

    //TODO: Refactor this monstrosity
    private boolean isBoundingColliding(Collider other){
        return boundingCircleCenter.subtract(other.boundingCircleCenter)
                .add(transform.getGlobalPos()).subtract(other.transform.getGlobalPos()).length() 
                <= boundingCircleRadius + other.boundingCircleRadius;
    }

    public boolean isCollidingWith(Collider other){
        // First simple bounding circle pass
        return isColliding = isBoundingColliding(other);

    }

    @Override
    public void draw(Renderer ctx) {
        // Debug only
        Shape circleShape = new PolygonShape(boundingCircleCenter.getX(), boundingCircleCenter.getY(),
                                            (int)boundingCircleRadius, 12);
        Vec2 pos = transform.getGlobalPos();
        ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), circleShape.getPoints(),
                                     isColliding ? Color.LIME : Color.RED);

        isColliding = false;
    }

}