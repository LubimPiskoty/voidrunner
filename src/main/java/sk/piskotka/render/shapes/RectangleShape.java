package sk.piskotka.render.shapes;

public class RectangleShape extends Shape{
    private int w;
    private int h;

    public RectangleShape(int x, int y, int w, int h) {
        super(x, y);
        this.w = w;
        this.h = h;
    }
    
    @Override
    public Shape create() {
        addPoint(x, y);
        addPoint(x+w, y);
        addPoint(x+w, y+h);
        addPoint(x, y+h);
        return this;
    }

    @Override
    public Shape clone() {
        return new RectangleShape(x, y, w, h).create();
    }
    
}
