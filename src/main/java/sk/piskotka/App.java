package sk.piskotka;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import sk.piskotka.input.Controller;
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

        // Game loop
        Renderer renderer = new Renderer(canvas, WIDTH, HEIGHT);
        Controller controller = new Controller(scene);
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
                    if (counter > 50){
                        stage.setTitle(String.format("Gametitle: %03d fps %.2fms", (int)(1/low), low*1000));
                        low = 0;
                        counter = 0;
                    }
                    gameManager.run(controller, dt);
                    controller.update();
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
 