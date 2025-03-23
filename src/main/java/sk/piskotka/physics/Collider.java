package sk.piskotka.physics;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.util.Pair;
import sk.piskotka.GameManager;
import sk.piskotka.logger.Logger;
import sk.piskotka.render.Drawable;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.Shape;

// Inspiration from
// https://timallanwheeler.com/blog/2024/08/01/2d-collision-detection-and-resolution/
public class Collider implements Drawable {
    //TODO: Check if shape is convex and counter clockwise
    private Transform parentTransform;
    private Shape colliderShape;
    private boolean isTrigger;
    private boolean isColliding;

    Collider(PhysicsBody pBody, Shape colliderShape, boolean isTrigger){
        if (colliderShape == null || colliderShape.getPoints().size() < 3)
            Logger.throwError(getClass(), "Invalid shape passed. Try to check vertex count or null ref");
        if (pBody == null)
            Logger.throwError(getClass(), "Invalid pBody ref passed");

        this.parentTransform = pBody;
        this.colliderShape = colliderShape;
        this.isTrigger = isTrigger;
        this.isColliding = false;
    }

    // A simple collision result container.
    public class CollisionResult {
        public boolean isColliding;
        public Vec2 normal;              // Collision (contact) normal
        public double penetration;       // Penetration depth
        public List<Vec2> contactPoints; // One or more contact points

        public CollisionResult() {
            isColliding = false;
            normal = Vec2.ZERO();
            penetration = Double.POSITIVE_INFINITY;
            contactPoints = new ArrayList<>();
        }
    }

    /**
     * Checks if this shape is colliding with another shape using the Separating Axis Theorem.
     */
    public boolean isCollidingWith(Collider other) {
        List<Vec2> polyA = this.transformedPoints();
        List<Vec2> polyB = other.transformedPoints();
        
        // Get all the axes to test from both polygons.
        List<Vec2> axes = new ArrayList<>();
        axes.addAll(getAxes(polyA));
        axes.addAll(getAxes(polyB));
        
        // For each axis, project both polygons.
        for (Vec2 axis : axes) {
            double[] projectionA = projectPolygon(polyA, axis);
            double[] projectionB = projectPolygon(polyB, axis);
            // "Axis: " + axis + ", Projection A: [" + projectionA[0] + ", " + projectionA[1] + "], Projection B: [" + projectionB[0] + ", " + projectionB[1] + "]"
            Logger.logDebug(getClass(), String.format("Axis: %s, ProjA: [%.2f, %.2f], ProjB: [%.2f, %.2f]", axis, projectionA[0], projectionA[1], projectionB[0], projectionB[1]));
            // If there is no overlap on this axis, then there is a separating axis.
            if (!isOverlaping(projectionA, projectionB)) {
                Logger.logDebug(getClass(), "Separating axis found on " + axis);
                return false; // Separating axis found, no collision.
            }
        }
        // No separating axis found; collision must exist.
        return true;
    }
    
    /**
     * Given a list of vertices, compute the perpendicular (normalized) vectors for each edge.
     */
    private List<Vec2> getAxes(List<Vec2> vertices) {
        List<Vec2> axes = new ArrayList<>();
        int count = vertices.size();
        for (int i = 0; i < count; i++) {
            Vec2 p1 = vertices.get(i);
            Vec2 p2 = vertices.get((i + 1) % count);
            // The edge vector is from p1 to p2.
            Vec2 edge = p1.subtract(p2);
            // The perpendicular vector to the edge.
            Vec2 normal = edge.normal().normalized();
            axes.add(normal);

            //DEBUG
            GameManager.getRenderer().drawVector(p1.add(p2).multiply(0.5), normal.multiply(50), Color.PURPLE);
        }
        return axes;
    }
    
    /**
     * Projects a polygon onto the given axis and returns the minimum and maximum values.
     */
    private double[] projectPolygon(List<Vec2> vertices, Vec2 axis) {
        double min = vertices.get(0).dot(axis);
        double max = min;
        for (Vec2 v : vertices) {
            double projection = v.dot(axis);
            if (projection < min) {
                min = projection;
            } else if (projection > max) {
                max = projection;
            }
        }
        return new double[]{min, max};
    }
    
    /**
     * Checks if two projection ranges [minA, maxA] and [minB, maxB] overlap.
     */
    private boolean isOverlaping(double[] projA, double[] projB) {
        return (projA[1] > projB[0] && projB[1] > projA[0]);
    }
    
    public boolean isColliding(){
        return isColliding;
    }

    public void onCollision(){
        isColliding = true; //TODO: temporary solution
    }

    public Shape transformedShape(Shape shape){
        return shape.rotated(parentTransform.getLocalRot()).moved(parentTransform.getGlobalPos());
    }

    public List<Vec2> transformedPoints(){
        return transformedShape(colliderShape).getPoints();
    }

    @Override
    public void draw(Renderer ctx) {
        ctx.drawPolygon(transformedPoints(), isColliding ? Color.RED : Color.LIME);
        isColliding = false;
    };

}