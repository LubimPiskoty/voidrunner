package sk.piskotka.physics;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DebugGraphics;

import javafx.scene.paint.Color;
import javafx.util.Pair;
import sk.piskotka.GameManager;
import sk.piskotka.logger.Logger;
import sk.piskotka.shapes.Shape;

// Inspiration from
// https://timallanwheeler.com/blog/2024/08/01/2d-collision-detection-and-resolution/
public class Collider {
    //TODO: Check if shape is convex and counter clockwise
    private List<Vec2> edgeNormals;
    private Transform parentTransform;
    private Shape colliderShape;

    Collider(PhysicsBody pBody, Shape colliderShape){
        if (colliderShape == null || colliderShape.getPoints().size() < 3)
            Logger.throwError(getClass(), "Invalid shape passed. Try to check vertex count or null ref");
        if (pBody == null)
            Logger.throwError(getClass(), "Invalid pBody ref passed");

        this.edgeNormals = new ArrayList<>();
        this.parentTransform = pBody;
        this.colliderShape = colliderShape;
    }

    public void calculateEdgeNormals(){
        List<Vec2> points = transformedPoints();
        edgeNormals.clear();

        for(int i = 0; i < points.size(); i++){
            Vec2 curr = points.get(i);
            Vec2 next = points.get((i+1) % points.size());
            Vec2 edgeNormal = curr.subtract(next).normal().normalized();
            edgeNormals.add(edgeNormal);
        }
    }

    private Pair<Double, Double> projectPolygonAlongDirection(Vec2 normal){
        double projLow = Double.POSITIVE_INFINITY;
        double projHigh= Double.NEGATIVE_INFINITY;

        for (Vec2 p : transformedPoints()){
            double projected = normal.dot(p);
            projLow = Math.min(projected, projLow);
            projHigh = Math.max(projected, projHigh);
        }

        return new Pair<Double, Double>(projLow, projHigh);
    }

    public void drawEdgeNormals(Color color){
        List<Vec2> points = transformedPoints();
        for(int i = 0; i < edgeNormals.size(); i++){
            Vec2 curr = points.get(i);
            Vec2 next = points.get((i+1) % points.size());
            Vec2 edgeMidpoint = curr.add(next).multiply(0.5);

            GameManager.getRenderer().drawVector(edgeMidpoint, edgeNormals.get(i).multiply(50), color);
        }
    }

    public List<Vec2> transformedPoints(){
        Shape cpy = colliderShape.copy();
        Shape cpyRot = cpy.rotated(parentTransform.getLocalRot()).moved(parentTransform.getGlobalPos());

        return cpyRot.getPoints(); 
    }

}
