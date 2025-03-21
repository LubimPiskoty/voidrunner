package sk.piskotka.shapes;

public class RectangleShape extends Shape{

    public RectangleShape(int x, int y, int w, int h) {
        addPoint(x, y);
        addPoint(x+w, y);
        addPoint(x+w, y+h);
        addPoint(x, y+h);
    }
    
}
