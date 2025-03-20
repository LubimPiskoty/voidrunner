package sk.piskotka.render.shapes;

import java.util.ArrayList;
import java.util.List;

import sk.piskotka.physics.Vec2;

// Implemented using Prototype Creational Patttern
public abstract class Shape {
    protected int x;
    protected int y;
    private List<Vec2> points;

    protected Shape(int x, int y){
        this.x = x;
        this.y = y;
        points = new ArrayList<>();
    }

    protected Shape(Shape copy){
        this.x = copy.x;
        this.y = copy.y;
        this.points = new ArrayList<>(copy.points);
    }

    public List<Vec2> getPoints() {
        return points;
    }

    protected void addPoint(int x, int y){
        points.add(new Vec2(x, y));
    }
    
    public Shape rotatedShape(double angle) {
        Shape copy = this.clone();
        copy.clearPoints();
        for(Vec2 p : points){
            Vec2 rotated = p.rotated(angle);
            copy.addPoint(rotated.getX(), rotated.getY());
        }
        return copy;
    }

    private void clearPoints(){
        this.points.clear();
    }

    public boolean isEmpty(){
        return points.size() == 0;
    }

    public abstract Shape create();
    public abstract Shape clone();
}
