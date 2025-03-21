package sk.piskotka.shapes;

import java.util.ArrayList;
import java.util.List;

import sk.piskotka.physics.Vec2;

// Implemented using Prototype Creational Patttern
public class Shape {
    private List<Vec2> points;

    protected Shape(){
        points = new ArrayList<>();
    }

    protected Shape(Shape copy){
        this.points = deepCopyPoints(copy);
    }

    private List<Vec2> deepCopyPoints(Shape copy){
        List<Vec2> list = new ArrayList<>();
        for (Vec2 v : copy.getPoints())
            list.add(new Vec2(v));
        return list;
    }

    public List<Vec2> getPoints() {
        return points;
    }

    protected void addPoint(int x, int y){
        points.add(new Vec2(x, y));
    }
    
    public Shape rotated(double angle) {
        Shape copy = copy();
        for(Vec2 p : copy.getPoints()){
            p.set(p.rotated(angle));
        }
        return copy;
    }

    public Shape moved(Vec2 offset) {
        Shape copy = copy();
        for(Vec2 p : copy.getPoints()){
            p.set(p.add(offset));
        }
        return copy;
    }

    private void clearPoints(){
        this.points.clear();
    }

    public boolean isEmpty(){
        return points.size() == 0;
    }

    public Shape copy(){
        return new Shape(this);
    }
}
