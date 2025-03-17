package sk.piskotka.render;

import java.nio.IntBuffer;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Renderer {
    private int width, height;
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
        updateScreen();
    }

    public void drawCross(int x, int y, int r, Color color){
        for(int i = -r; i < r; i++){
            drawPixelNoUpdate(x+i, y, color);
            drawPixelNoUpdate(x, y+i, color);
        }
        updateScreen();
    }

    private void drawPixelNoUpdate(int x, int y, Color color) {
        if (x >= width || y >= height || x < 0 || y < 0){
            //System.out.println("(E) In drawPixel args out of canvas size!!");
            return;
        }
            
        pixels[(x % width) + (y * width)] = getColor(color);
    }

    public void drawPixel(int x, int y, Color color){
        drawPixelNoUpdate(x, y, color);
        updateScreen();
    }

    public void updateScreen(){
        pixelBuffer.updateBuffer(b -> null);
    }

    public static int getColor(Color color) {
        return 255 << 24 | (int)(color.getRed()*255) << 16 | (int)(color.getGreen()*255) << 8 | (int)(color.getBlue()*255);
    }
}
