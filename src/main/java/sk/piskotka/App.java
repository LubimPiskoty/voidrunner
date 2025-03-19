/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package sk.piskotka;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Renderer;
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
        Vec2 mousePos = Vec2.ZERO();
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
        scene.setOnMouseMoved(
            new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent event) {
                    mousePos.set(event.getSceneX(), event.getSceneY());
                }
            });
        scene.setOnMousePressed(
            new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent event) {
                    mousePos.set(event.getSceneX(), event.getSceneY());
                    input.add(event.getButton().toString());
                }
            });
        scene.setOnMouseReleased(
            new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent event) {
                    mousePos.set(event.getSceneX(), event.getSceneY());
                    input.remove(event.getButton().toString());
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
                    gameManager.run(input, mousePos, dt);
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
 