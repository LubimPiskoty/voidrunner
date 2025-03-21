package sk.piskotka;

import java.util.ArrayList;
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

    public GameManager(Renderer ctx) {
        // Create the singleton
        if (instance == null)
            instance = this;
        else
            Logger.throwError(getClass(), "GameManager singleton was already created!");
        
        // Init create all other variables
        this.targetFrametime = 0.016;
        this.ctx = ctx;
        this.startTime = System.currentTimeMillis();
        randomGenerator = new Random();
        level = new Level();
        
        level.Create(new PlayerShip(ctx.getWidth()/2, ctx.getHeight()/2));
        level.Create(new Asteroid(300, 300, Math.PI/12).randomized());
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

        player.move(inputVec.normalized());
        player.aim(mousePos);
    }

    public void run(List<String> inputs, Vec2 mousePos, double dt) {
        //System.out.println("Events: " + inputs);
        if (isRunning){
            ctx.clearScreen(Color.BLACK);
            processEvents(inputs, mousePos);
            level.update(dt/2);
            level.render(ctx);
            level.DestroyMarked();
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
