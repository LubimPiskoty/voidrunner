package sk.piskotka.render;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.Vec2;

public class Renderer {
    private int width, height;
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    private int[] pixels;
    private PixelBuffer<IntBuffer> pixelBuffer;

    public Renderer(Group root, int width, int height) {
        this.width = width;
        this.height = height;
        // set up pixel buffer
        IntBuffer buffer = IntBuffer.allocate(width * height);
        pixels = buffer.array();
        pixelBuffer = new PixelBuffer<>(width, height, buffer, PixelFormat.getIntArgbPreInstance());

        WritableImage image = new WritableImage(pixelBuffer);

        root.getChildren().add(new ImageView(image));
    }

    public void clearScreen(Color color){
        for(int i = 0; i < width*height; i++)
            pixels[i] = getColor(color);
    }

    public void drawPolygonWithOffset(int x, int y, List<Vec2> points, Color color){
        if (points.size() == 0){
            Logger.LogWarning(this, "drawPolygonWithOffset was called with empty points list!!");
            return;
        }
        Vec2 prev, next;
        for(int i = 1; i < points.size(); i++){
            prev = points.get(i-1);
            next = points.get(i);
            drawLine(prev.getX()+x, prev.getY()+y, next.getX()+x, next.getY()+y, color);
        }
        prev = points.get(0);
        next = points.get(points.size()-1);
        drawLine(next.getX()+x, next.getY()+y, prev.getX()+x, prev.getY()+y, color); 
    }

    public void drawPolygon(List<Vec2> points, Color color){
        drawPolygonWithOffset(0, 0, points, color);
    }

    public void drawCross(int x, int y, int r, Color color){
        drawLine(x-r, y-r, x+r, y+r, color);
        drawLine(x-r, y+r, x+r, y-r, color);
    }

    public void drawLine(int x1, int y1, int x2, int y2, Color color){
        // Ensure y2 is bigger than y1, same for x2,x1
        int tmp;


        // Handle vertical and horizontal lines
        if (Math.abs(x2-x1) > Math.abs(y2-y1)){
            // Line is horizontal ish
            if (x1 > x2){
                tmp = x1;
                x1 = x2;
                x2 = tmp;
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            List<Integer> vals = linearFunc(x1, y1, x2, y2);
            for(int i = x1; i < x2; i++)
                drawPixel(i, vals.get(i-x1), color);
            
        } else {
            if (y1 > y2){
                tmp = x1;
                x1 = x2;
                x2 = tmp;
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            List<Integer> vals = linearFunc(y1, x1, y2, x2);
            for(int i = y1; i < y2; i++)
                drawPixel(vals.get(i-y1), i, color);
            
        }
    }

    private void drawPixel(int x, int y, Color color) {
        if (x >= width || y >= height || x < 0 || y < 0){
            //System.out.println("(E) In drawPixel args out of canvas size!!");
            return;
        }
            
        pixels[(x % width) + (y * width)] = getColor(color);
    }

    // Refactored version of draw line to save lines
    // y2 and x2 must be bigger than their respective counterparts
    private List<Integer> linearFunc(int x1, int y1, int x2, int y2) {
        List<Integer> vals = new ArrayList<>();
        if (x2-x1 == 0){ // Vertical line
            for(int i = y1; i < y2; i++)
                vals.add(x1);
            return vals;
        }

        double k = (double)(y2-y1)/(double)(x2-x1);
        for(int i = x1; i < x2; i++){
            int j = (int)(k*(i-x1)+y1);
            vals.add(j);
        }
        return vals;
    }

    public void updateScreen(){
        pixelBuffer.updateBuffer(b -> null);
    }

    public static int getColor(Color color) {
        return 255 << 24 | (int)(color.getRed()*255) << 16 | (int)(color.getGreen()*255) << 8 | (int)(color.getBlue()*255);
    }
}
