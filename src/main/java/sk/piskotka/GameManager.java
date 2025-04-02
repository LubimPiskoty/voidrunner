package sk.piskotka;

import java.util.List;
import java.util.Random;

import sk.piskotka.camera.Camera;
import sk.piskotka.camera.FollowerCamera;
import sk.piskotka.enviroment.Asteroid;
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

    void processEvents(List<String> inputs, Vec2 mousePos){
        PlayerShip player = level.getPlayer();
        Vec2 inputVec = Vec2.ZERO();
        if (inputs.contains("W"))
            inputVec = inputVec.add(Vec2.DOWN());
        if (inputs.contains("S"))
            inputVec = inputVec.add(Vec2.UP());
        if (inputs.contains("A"))
            inputVec = inputVec.add(Vec2.LEFT());
        if (inputs.contains("D"))
            inputVec = inputVec.add(Vec2.RIGHT());
        if (inputs.contains("PRIMARY"))
            player.attemptToShoot();
        
        //Toggle debug mode
        if (inputs.contains("G"))
            this.isDebug = true;
        if (inputs.contains("H"))
            this.isDebug = false;
        if (inputs.contains("H"))
            level.printLevelHierarchy();

        player.move(inputVec.normalized());
        mousePos = mousePos.add(renderer.getActiveCamera().getPosition()).multiply(1/renderer.getActiveCamera().getZoom());
        Logger.logDebug(getClass(), "Mouse pos is: " + mousePos);
        player.aim(mousePos);
    }

    public void run(List<String> inputs, Vec2 mousePos, double dt) {
        //System.out.println("Events: " + inputs);
        if (isRunning){
            processEvents(inputs, mousePos);
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
