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
 * The main entry point for the Voidrunner game application. This class extends {@link Application}
 * and is responsible for setting up the game window, initializing key components, and running the
 * main game loop.
 * 
 * <p>The application initializes the game window, creates the necessary input controllers,
 * rendering system, and manages the game loop with a fixed frame rate. The game loop is powered
 * by {@link AnimationTimer} to ensure smooth updates and rendering.</p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Initializes and configures the game window using {@link Stage} and {@link Scene}.</li>
 *     <li>Creates and sets up the main canvas for rendering with {@link Canvas}.</li>
 *     <li>Manages the game loop with a consistent frame rate and controls FPS display.</li>
 *     <li>Handles user input through the {@link Controller} class.</li>
 *     <li>Uses {@link Renderer} to render the game objects to the screen.</li>
 * </ul>
 * 
 * @author Piskotka
 */
public class App extends Application {

    /** The title of the game displayed in the window. */
    String gameTitle = "Voidrunner";

    /** The width of the game window. */
    int WIDTH = 1280;

    /** The height of the game window. */
    int HEIGHT = 960;

    /**
     * Initializes the game window, input controllers, and game loop.
     * 
     * @param stage The primary stage (window) for the game.
     */
    @Override
    public void start(Stage stage) {
        // Window init
        stage.setTitle(gameTitle);
        stage.setResizable(false);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        
        // Create a canvas to render the game
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        Scene scene = new Scene(new Group(canvas));
        stage.setScene(scene);

        // Set up the renderer, controller, and game manager
        Renderer renderer = new Renderer(canvas, WIDTH, HEIGHT);
        Controller controller = new Controller(scene);
        GameManager gameManager = new GameManager(renderer);

        // Start the game loop
        new AnimationTimer() {
            double targetMs = 0.001;  // Target frame time in seconds
            long lastNanoTime = System.nanoTime();
            double low = 0;  // Tracks the lowest delta time (frame time)
            int counter = 0; // Counts frames to update the FPS display

            /**
             * Handles the game loop. This method is invoked continuously during the application
             * runtime to update the game state, render the game, and manage input events.
             * 
             * @param currentNanoTime The current time in nanoseconds for this frame.
             */
            public void handle(long currentNanoTime) {
                double dt = (currentNanoTime - lastNanoTime) / 1000000000.0;
                
                // Only proceed if delta time is large enough to process the frame
                if (dt >= targetMs) {
                    // Update the FPS display every 50 frames
                    if (counter > 50) {
                        stage.setTitle(String.format("Gametitle: %03d fps %.2fms", 
                                                     (int)(1 / low), low * 1000));
                        low = 0;
                        counter = 0;
                    }
                    
                    // Run game logic and update controller
                    gameManager.run(controller, dt);
                    controller.update();
                    lastNanoTime = currentNanoTime;

                    // Track the lowest frame time (useful for performance profiling)
                    if (dt > low) low = dt;
                    counter++;
                }
            }
        }.start();

        // Display the window
        stage.show();
    }

    /**
     * The main method to launch the game application.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        launch();
    }
}
