package sk.piskotka.camera;

import sk.piskotka.physics.Vec2;
import sk.piskotka.shapes.Shape;

@SuppressWarnings("unused")
public abstract class Camera{
    protected Vec2 position;
    protected double zoom;
    public double getZoom() {return zoom;}
    public void setZoom(double zoom) {this.zoom = zoom;}

    public Vec2 getPosition() {return position.multiply(zoom);}

    protected Camera(Vec2 position) {
        this.position = position;
        this.zoom = 1;
    }

    public Shape applyCamera(Shape shape){
        return shape.scaled(zoom).moved(getPosition().multiply(-1));
    }

    public Vec2 applyCamera(Vec2 point){
        return point.multiply(zoom).add(getPosition().multiply(-1));
    }

    public void update(double dt){}
}
