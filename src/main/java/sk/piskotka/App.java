/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package sk.piskotka;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sk.piskotka.Physics.GameObject;
import sk.piskotka.Physics.Vec2;
import sk.piskotka.render.Renderer;
import sk.piskotka.ship.PlayerShip;
import sk.piskotka.ship.Spaceship;
/**
 *
* @author piskotka
*/
public class App extends Application{
    String gameTitle = "Voidrunner";
    int WIDTH = 1280;
    int HEIGHT = 960;

    @Override
    public void start(Stage stage) {
        // Window init
        stage.setTitle(gameTitle);
        stage.setResizable(false);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        Group root = new Group();
        Renderer renderer = new Renderer(root, WIDTH, HEIGHT);
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Input handeling
        ArrayList<String> input = new ArrayList<String>();
        scene.setOnKeyPressed(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    // only add once... prevent duplicates 
                    if ( !input.contains(code) )
                        input.add( code );
                }
            });
        scene.setOnKeyReleased(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    input.remove( code );
                }
            });
        
        // Game loop
        GameManager gameManager = new GameManager(renderer);
        new AnimationTimer()
        {
            long lastNanoTime = System.nanoTime();
            public void handle(long currentNanoTime)
            {
                double dt = (currentNanoTime - lastNanoTime) / 1000000000.0;
                if (dt > gameManager.targetFrametime){
                    stage.setTitle(String.format("Gametitle: %.2fms", dt*1000));
                    gameManager.run(input, dt);
                    lastNanoTime = currentNanoTime;
                }
            }
        }.start();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
 