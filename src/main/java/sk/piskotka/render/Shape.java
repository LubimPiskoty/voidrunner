package sk.piskotka.render;

import java.util.ArrayList;
import java.util.List;

import sk.piskotka.physics.Vec2;

public class Shape {
    private List<Vec2> points;

    private Shape(){
        points = new ArrayList<>();
    }

    private Shape(Shape copy){
        this.points = new ArrayList<>(copy.points);
    }

    public List<Vec2> getPoints() {
        return points;
    }

    public void addPoint(int x, int y){
        points.add(new Vec2(x, y));
    }
    
    public Shape rotatedShape(double angle) {
        Shape copy = new Shape();
        for(Vec2 p : points){
            double currAngle = Math.atan2(p.getY(), p.getX());
            double r = p.length();
            copy.addPoint((int)(Math.cos(currAngle+angle)*r), (int)(Math.sin(currAngle+angle)*r));
        }
        return copy;
    }

    public static Shape CreateRectangle(int x, int y, int w, int h){
        Shape s = new Shape();
        s.addPoint(x, y);
        s.addPoint(x+w, y);
        s.addPoint(x+w, y+h);
        s.addPoint(x, y+h);
        return s;
    }

    public static Shape CreateNGon(int x, int y, int r, int n){
        Shape s = new Shape();

        for(int i = 0; i < n; i++){
            double angle = Math.PI*2 * ((double)i/(double)n);
            s.addPoint((int)(x+Math.cos(angle)*r), (int)(y+Math.sin(angle)*r));
        }

        return s;
    }

    public static Shape CreateEmpty(){
        Shape s = new Shape();
        s.addPoint(0, 0);
        s.addPoint(1, 0);
        s.addPoint(0, 1);
        s.addPoint(1, 1);
        return s;
    }
    
}
