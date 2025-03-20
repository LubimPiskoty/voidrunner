package sk.piskotka.render.shapes;

public class PolygonShape extends Shape {
    private int r;
    private int vertexCount;
    
    public PolygonShape(int x, int y, int r, int vertexCount){
        super(x, y);
        this.r = r;
        this.vertexCount = vertexCount;

    }
    
    @Override
    public Shape create() {
        for(int i = 0; i < vertexCount; i++){
            double angle = Math.PI*2 * ((double)i/(double)vertexCount);
            addPoint((int)(x+Math.cos(angle)*r), (int)(y+Math.sin(angle)*r));
        }
        
        return this;
    }
    
    @Override
    public Shape clone() {
        return new PolygonShape(x, y, r, vertexCount).create();
    }
}
