package sk.piskotka;

import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;
import sk.piskotka.enviroment.Asteroid;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Renderer;
import sk.piskotka.ship.PlayerShip;

public class GameManager {
    private static GameManager instance;
    public static boolean isRunning = true;
    public double targetFrametime;
    public Random randomGenerator;
    public long startTime;
    public Level level;
    private Renderer ctx;

    private boolean isDebug;
    public static boolean isDebug() {return getInstance().isDebug;}

    public GameManager(Renderer ctx) {
        // Create the singleton
        if (instance == null)
            instance = this;
        else
            Logger.throwError(getClass(), "GameManager singleton was already created!");
        
        // Init create all other variables
        this.isDebug = false;
        this.targetFrametime = 0.016;
        this.ctx = ctx;
        this.startTime = System.currentTimeMillis();
        randomGenerator = new Random();
        level = new Level();
        
        level.create(new PlayerShip(ctx.getWidth()/2, ctx.getHeight()/2));
        level.create(new Asteroid(550, 550, 0));
        level.printLevelHierarchy();
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

        player.move(inputVec.normalized());
        player.aim(mousePos);
    }

    public void run(List<String> inputs, Vec2 mousePos, double dt) {
        //System.out.println("Events: " + inputs);
        if (isRunning){
            ctx.clearScreen(Color.BLACK);
            processEvents(inputs, mousePos);
            level.update(dt);
            level.render(ctx);
            ctx.updateScreen();
        }
    }
    
    public static GameManager getInstance(){
        return instance;
    }

    public static Level getLevel(){
        return instance.level;
    }

    public static Renderer getRenderer(){
        return instance.ctx;
    }
}
