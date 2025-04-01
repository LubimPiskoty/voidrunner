package sk.piskotka.shapes;

import java.util.ArrayList;
import java.util.List;

import sk.piskotka.logger.Logger;
import sk.piskotka.physics.Vec2;

// Implemented using Prototype Creational Patttern
public class Shape {
    private int capacity;
    private int size;
    public int getSize() {return size;}

    private double[] pointsX;
    public double[] getPointsX() {return pointsX;}

    private double[] pointsY;
    public double[] getPointsY() {return pointsY;}

    private void init(int capacity){
        this.capacity = capacity;
        this.size = 0;
        pointsX = new double[capacity];
        pointsY = new double[capacity];
    }

    protected Shape(int capacity){
        init(capacity);
    }
    
    protected Shape(Shape copy){
        init(copy.size);
        deepCopyPoints(copy);
    }

    private void deepCopyPoints(Shape copy){
        for (int i = 0; i < copy.size; i++){
            pointsX[i] = copy.getPointsX()[i];
            pointsY[i] = copy.getPointsY()[i];
        }
        size = copy.size;
    }

    protected void addPoint(double x, double y){
        if (size == capacity)
            Logger.throwError(getClass(), "This shape cannot hold any more points!!");
        pointsX[size] = x;
        pointsY[size] = y;
        size++;
    }
    
    public Shape rotated(double angle) {
        Shape copy = copy();
        for(int i = 0; i < size; i++){
            Vec2 rotated = new Vec2(pointsX[i], pointsY[i]).rotated(angle);
            copy.pointsX[i] = rotated.getX();
            copy.pointsY[i] = rotated.getY();
        }
        return copy;
    }

    public List<Vec2> getPoints(){
        List<Vec2> points = new ArrayList<>();
        for(int i = 0; i < size; i++)
            points.add(new Vec2(pointsX[i], pointsY[i]));

        return points;
    }

    public Shape moved(Vec2 offset) {
        Shape copy = copy();
        for(int i = 0; i < size; i++){
            Vec2 rotated = new Vec2(pointsX[i], pointsY[i]).add(offset);
            copy.pointsX[i] = rotated.getX();
            copy.pointsY[i] = rotated.getY();
        }
        return copy;
    }

    public Shape scaled(double factor){
        Shape copy = copy();
        for(int i = 0; i < size; i++){
            Vec2 rotated = new Vec2(pointsX[i], pointsY[i]).multiply(factor);
            copy.pointsX[i] = rotated.getX();
            copy.pointsY[i] = rotated.getY();
        }
        return copy;
    }
    
    public boolean isEmpty(){
        return size == 0;
    }

    public Shape copy(){
        return new Shape(this);
    }
}
