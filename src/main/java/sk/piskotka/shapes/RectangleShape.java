package sk.piskotka.shapes;

public class RectangleShape extends Shape{

    public RectangleShape(int x, int y, int w, int h) {
        super(4);
        addPoint(x, y);
        addPoint(x+w, y);
        addPoint(x+w, y+h);
        addPoint(x, y+h);
    }
    
}
