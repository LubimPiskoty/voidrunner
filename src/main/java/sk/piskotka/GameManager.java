package sk.piskotka;

import java.util.Random;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import sk.piskotka.camera.Camera;
import sk.piskotka.camera.FollowerCamera;
import sk.piskotka.enviroment.Asteroid;
import sk.piskotka.input.Controller;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Renderer;
import sk.piskotka.ship.CruiserEnemy;
import sk.piskotka.ship.PlayerShip;
import sk.piskotka.ship.TankEnemy;

/**
 * The {@code GameManager} class is responsible for managing the game's core logic, including event processing,
 * level management, camera control, and game state updates. It serves as the main controller for the game flow.
 * It is a singleton that ensures there is only one instance of the game manager during runtime.
 * 
 * <p>The {@code GameManager} processes player input, handles updates to the game objects, and coordinates
 * rendering through the {@link Renderer}. It also manages game objects like the player ship and enemies,
 * and handles the camera's movement and zoom based on the player's position.</p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Processes player input for movement and shooting.</li>
 *     <li>Handles updates for the level, enemies, and game objects.</li>
 *     <li>Controls the game camera, including zoom and following behavior.</li>
 *     <li>Maintains a singleton instance of the {@code GameManager} for centralized game control.</li>
 *     <li>Handles toggling of debug mode and printing of level hierarchy.</li>
 * </ul>
 * 
 * <p>The {@code GameManager} also integrates with the {@code Controller} class for input, {@code Level}
 * for level data, and various game objects like {@link PlayerShip} and enemies.</p>
 * 
 * @author Piskotka
 */
public class GameManager {
    
    /** The singleton instance of the {@code GameManager}. */
    private static GameManager instance;

    /** Flag indicating whether the game is currently running. */
    public static boolean isRunning = true;

    /** Random number generator used for various game-related randomness. */
    private final Random randomGenerator;

    /** The level object representing the current game level. */
    private Level level;

    /** Flag indicating whether the game is in debug mode. */
    private boolean isDebug;

    /** The renderer responsible for drawing the game world to the screen. */
    private final Renderer renderer;

    /**
     * Creates an instance of {@code GameManager} and initializes the game components.
     * This includes setting up the player ship, enemies, asteroids, and the camera.
     * The {@code GameManager} is initialized as a singleton to ensure only one instance
     * exists throughout the game lifecycle.
     * 
     * @param renderer The renderer responsible for rendering the game scene.
     */
    public GameManager(Renderer renderer) {
        // Create the singleton instance
        if (instance == null)
            instance = this;
        else
            Logger.throwError(getClass(), "GameManager singleton was already created!");

        // Initialize variables
        this.isDebug = false;
        this.renderer = renderer;
        this.randomGenerator = new Random();
        
        // Initialize and set up the game level
        level = new Level();
        level.create(new PlayerShip(0, 0, 100, 100));
        level.create(new Asteroid(100, -200, 0.2));
        level.create(new Asteroid(-200, 100, -0.3));
        level.create(new TankEnemy(400, 400));
        level.create(new CruiserEnemy(-400, -400));

        // Set up the camera to follow the player
        Vec2 center = new Vec2(renderer.getWidth(), renderer.getHeight()).multiply(0.5);
        Camera camera = new FollowerCamera(level.getPlayer(), center, 2);
        camera.setZoom(0.6);
        renderer.setActiveCamera(camera);
    }

    /**
     * Processes player input and updates the player's state based on controller actions.
     * This method checks if movement keys (W, A, S, D) are pressed, handles mouse events, and
     * toggles debug mode.
     * 
     * @param controller The controller handling user input.
     */
    void processEvents(Controller controller) {
        PlayerShip player = level.getPlayer();
        Vec2 inputVec = Vec2.ZERO();

        // Handle movement input
        if (controller.isPressed(KeyCode.W))
            inputVec = inputVec.add(Vec2.DOWN());
        if (controller.isPressed(KeyCode.S))
            inputVec = inputVec.add(Vec2.UP());
        if (controller.isPressed(KeyCode.A))
            inputVec = inputVec.add(Vec2.LEFT());
        if (controller.isPressed(KeyCode.D))
            inputVec = inputVec.add(Vec2.RIGHT());
        
        // Handle shooting input
        if (controller.isPressed(MouseButton.PRIMARY))
            player.attemptToShoot();
        
        // Toggle debug mode
        if (controller.isJustPressed(KeyCode.G))
            this.isDebug = !this.isDebug;

        // Print the level hierarchy in the debug mode
        if (controller.isJustPressed(KeyCode.H))
            level.printLevelHierarchy();

        // Move the player and aim the ship towards the mouse position
        player.move(inputVec.normalized());
        Vec2 mousePos = controller.getMousePos().add(renderer.getActiveCamera().getPosition())
                            .multiply(1 / renderer.getActiveCamera().getZoom());
        player.aim(mousePos);
    }

    /**
     * Runs the game logic for each frame, processes input events, updates the level, and renders the
     * current state of the game.
     * 
     * @param controller The controller that processes user input.
     * @param dt The delta time between frames (used for smooth updates).
     */
    public void run(Controller controller, double dt) {
        if (isRunning) {
            processEvents(controller);
            level.update(dt);
            renderer.getActiveCamera().update(dt);  
            level.render(renderer);
        }
    }

    /**
     * Returns the singleton instance of the {@code GameManager}.
     * 
     * @return The singleton instance of {@code GameManager}.
     */
    public static GameManager getInstance() {
        return instance;
    }

    /**
     * Returns the current level being played.
     * 
     * @return The current level, or {@code null} if the instance is not yet created.
     */
    public static Level getLevel() {
        if (instance == null) {
            Logger.logWarning(GameManager.class, "Cannot get level because there is no GameManager instance");
            return null;
        }
        return instance.level;
    }

    /**
     * Returns the random number generator used by the game.
     * 
     * @return The random number generator instance.
     */
    public Random getRandomGenerator() {
        return randomGenerator;
    }

    /**
     * Returns whether the game is currently in debug mode.
     * 
     * @return {@code true} if the game is in debug mode, {@code false} otherwise.
     */
    public static boolean isDebug() {
        return getInstance().isDebug;
    }
}
