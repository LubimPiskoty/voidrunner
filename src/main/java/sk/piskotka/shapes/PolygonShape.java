package sk.piskotka.shapes;

public class PolygonShape extends Shape {
    
    public PolygonShape(double x, double y, double arrowSize, int vertexCount){
        super(vertexCount);
        for(int i = 0; i < vertexCount; i++){
            double angle = Math.PI*2 * ((double)i/(double)vertexCount);
            addPoint((int)(x+Math.cos(angle)*arrowSize), (int)(y+Math.sin(angle)*arrowSize));
        }
    }

}
