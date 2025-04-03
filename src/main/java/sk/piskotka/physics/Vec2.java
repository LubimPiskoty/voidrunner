package sk.piskotka.physics;

public final class Vec2 {
    private double x, y;

    public double getX() { return x; }
    public double getY() { return y; }

    // Constructors
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(Vec2 other) {
        this(other.x, other.y);
    }

    // Instance Methods
    public Vec2 add(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    public Vec2 multiply(double scalar) {
        return new Vec2(this.x * scalar, this.y * scalar);
    }

    public Vec2 subtract(Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vec2 normalized() {
        double l = length();
        return (Math.abs(l) < 0.000001) ? new Vec2(0, 0) : new Vec2(x / l, y / l);
    }
    
    // Rotates vector in counter-clock wise by angle in radians
    public Vec2 rotated(double angle){
        //TODO: Refactor into matrix mult
        double currAngle = Math.atan2(y, x);
        double r = length();
        return new Vec2(Math.cos(currAngle+angle)*r, Math.sin(currAngle+angle)*r);
    }


    public void set(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void set(Vec2 v){
        set(v.x, v.y);
    }

    public double getHeading() {
        return Math.atan2(y, x);
    }

    public double dot(Vec2 other){
        return x*other.x+y+other.y;
    }

    public Vec2 normal(){
        return new Vec2(-y, x);
    }


    //TODO: Refactor
    public Vec2 lerp(Vec2 target, double t) {
        return new Vec2(
            this.x + (target.x - this.x) * t,
            this.y + (target.y - this.y) * t
        );
    }


    // Static Factory Methods
    public static Vec2 ZERO() { return new Vec2(0, 0); }
    public static Vec2 ONES() { return new Vec2(1, 1); }
    public static Vec2 UP() { return new Vec2(0, 1); }
    public static Vec2 DOWN() { return new Vec2(0, -1); }
    public static Vec2 LEFT() { return new Vec2(-1, 0); }
    public static Vec2 RIGHT() { return new Vec2(1, 0); }

    public static Vec2 randomUnit() {
        Vec2 v = new Vec2(Math.random() - 0.5, Math.random() - 0.5);
        return v.normalized();
    }

    public static Vec2 fromHeading(double angle) {
        return new Vec2(Math.cos(angle), Math.sin(angle));
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vec2){
            Vec2 other = (Vec2)obj;
            return other.x == x && other.y == y;
        }
        return false;
    }
}
