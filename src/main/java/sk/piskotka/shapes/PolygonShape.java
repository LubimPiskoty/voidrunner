package sk.piskotka.shapes;

public class PolygonShape extends Shape {
    
    public PolygonShape(int x, int y, int r, int vertexCount){
        for(int i = 0; i < vertexCount; i++){
            double angle = Math.PI*2 * ((double)i/(double)vertexCount);
            addPoint((int)(x+Math.cos(angle)*r), (int)(y+Math.sin(angle)*r));
        }
    }

}
