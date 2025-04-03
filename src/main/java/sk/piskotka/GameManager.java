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

public class GameManager {
    private static GameManager instance;
    public static boolean isRunning = true;
    private Random randomGenerator;
    public Random getRandomGenerator() {return randomGenerator;}

    private Level level;
    private Renderer renderer;

    private boolean isDebug;
    public static boolean isDebug() {return getInstance().isDebug;}

    public GameManager(Renderer renderer) {
        // Create the singleton
        if (instance == null)
            instance = this;
        else
            Logger.throwError(getClass(), "GameManager singleton was already created!");
        
        // Init create all other variables
        this.isDebug = false;
        this.renderer = renderer;

        randomGenerator = new Random();
        //TODO: Extract this logic into level builder   
        level = new Level();
        
        level.create(new PlayerShip(0, 0, 100, 100));
        level.create(new Asteroid(100, -200, 0.2));
        level.create(new Asteroid(-200, 100, -0.3));
        level.create(new TankEnemy(400, 400));
        level.create(new CruiserEnemy(-400, -400));

        // Always add player before adding camera
        Vec2 center = new Vec2(renderer.getWidth(), renderer.getHeight()).multiply(0.5);
        Camera camera = new FollowerCamera(level.getPlayer(), center, 2);
        camera.setZoom(0.6);
        renderer.setActiveCamera(camera);
    }

    void processEvents(Controller controller){
        PlayerShip player = level.getPlayer();
        Vec2 inputVec = Vec2.ZERO();
        if (controller.isPressed(KeyCode.W))
            inputVec = inputVec.add(Vec2.DOWN());
        if (controller.isPressed(KeyCode.S))
            inputVec = inputVec.add(Vec2.UP());
        if (controller.isPressed(KeyCode.A))
            inputVec = inputVec.add(Vec2.LEFT());
        if (controller.isPressed(KeyCode.D))
            inputVec = inputVec.add(Vec2.RIGHT());
        if (controller.isPressed(MouseButton.PRIMARY))
            player.attemptToShoot();
        
        //Toggle debug mode
        if (controller.isJustPressed(KeyCode.G))
            this.isDebug = !this.isDebug;

        if (controller.isJustPressed(KeyCode.H))
            level.printLevelHierarchy();

        player.move(inputVec.normalized());
        Vec2 mousePos = controller.getMousePos().add(renderer.getActiveCamera().getPosition())
                            .multiply(1/renderer.getActiveCamera().getZoom());
        player.aim(mousePos);
    }

    public void run(Controller controller, double dt) {
        //System.out.println("Events: " + inputs);
        if (isRunning){
            processEvents(controller);
            level.update(dt);
            renderer.getActiveCamera().update(dt);  
            level.render(renderer);
        }
    }
    
    public static GameManager getInstance(){
        return instance;
    }

    public static Level getLevel(){
        return instance.level;
    }
}
