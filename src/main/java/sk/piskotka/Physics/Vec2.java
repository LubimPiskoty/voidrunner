package sk.piskotka.Physics;

public final class Vec2 {
    double x, y;

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    // Constructors
    public Vec2(double x, double y) {
        this.set(x, y);
    }

    public Vec2(Vec2 other){
        this.set(other);
    }

    // Methods
    public void set(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void set(Vec2 other){
        this.set(other.x, other.y);
    }

    public void add(Vec2 other){
        this.set(Vec2.add(this, other));
    }

    public void multiply(double scalar){
        this.set(Vec2.multiply(this, scalar));
    }

    // Static methods
    public static Vec2 add(Vec2 first, Vec2 second){
        return new Vec2(first.x + second.x, first.y + second.y);
    }

    public static Vec2 multiply(Vec2 vec, double scalar){
        return new Vec2(vec.x * scalar, vec.y * scalar);
    }

    public static Vec2 subtract(Vec2 first, Vec2 second){
        return new Vec2(Vec2.add(first, Vec2.multiply(second, -1)));
    }

    public static Vec2 ZERO(){
        return new Vec2(0, 0);
    }
    public static Vec2 ONES(){
        return new Vec2(1, 1);
    }

    public static Vec2 UP(){
        return new Vec2(0, 1);
    }
    public static Vec2 DOWN(){
        return new Vec2(0, -1);
    }
    public static Vec2 LEFT(){
        return new Vec2(-1, 0);
    }
    public static Vec2 RIGHT(){
        return new Vec2(1, 0);
    }

    public String toString(){
        return "(" + x + "," + y + ")";
    }
}
