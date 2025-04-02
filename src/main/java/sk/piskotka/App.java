package sk.piskotka;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
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
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        Scene scene = new Scene(new Group(canvas));
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
                    input.add(event.getButton().toString());
                }
            });
        scene.setOnMouseDragged(
            new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent event) {
                    mousePos.set(event.getSceneX(), event.getSceneY());
                }
            });
        scene.setOnMouseReleased(
            new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent event) {
                    input.remove(event.getButton().toString());
            }
        });
        // Game loop
        Renderer renderer = new Renderer(canvas, WIDTH, HEIGHT);
        GameManager gameManager = new GameManager(renderer);
        new AnimationTimer()
        {
            double targetMs = 0.001;
            long lastNanoTime = System.nanoTime();
            double low = 0;
            int counter = 0;
            public void handle(long currentNanoTime)
            {
                double dt = (currentNanoTime - lastNanoTime) / 1000000000.0;
                if (dt >= targetMs){
                    if (counter > 20){
                        stage.setTitle(String.format("Gametitle: %.2fms", low*1000));
                        low = 0;
                        counter = 0;
                    }
                    gameManager.run(input, mousePos, dt);
                    lastNanoTime = currentNanoTime;
                    
                    if (dt > low)
                        low = dt;
                    counter++;
                }
            }
        }.start();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
 