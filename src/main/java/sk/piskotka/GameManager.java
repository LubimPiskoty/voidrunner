package sk.piskotka;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;
import sk.piskotka.enviroment.Asteroid;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Vec2;
import sk.piskotka.render.Renderer;
import sk.piskotka.ship.PlayerShip;

public class GameManager {
    private static GameManager instance;
    public static boolean isRunning = true;
    public double targetFrametime;
    public Random randomGenerator;
    public long startTime;
    private List<PhysicsBody> world;
    private Renderer ctx;
    private PlayerShip player;

    public GameManager(Renderer ctx) {
        // Create the singleton
        if (instance == null)
            instance = this;
        else
            throw new Error("GameManager singleton was already created! There can only be one instance of GameManager");
        
        // Init create all other variables
        this.targetFrametime = 0.016;
        this.ctx = ctx;
        this.startTime = System.currentTimeMillis();
        Logger.LogInfo("Game is starting");
        randomGenerator = new Random();
        player = new PlayerShip(ctx.getWidth()/2, ctx.getHeight()/2);
        world = new ArrayList<>();
        
        CreateEntity(player);

        CreateEntity(new Asteroid(300, 300, Math.PI/12).randomized());
    }

    void processEvents(List<String> inputs, Vec2 mousePos){
        //TODO: Handle difference between onClick and pressed
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
            player.shoot();
        
        player.move(inputVec.normalized());
        player.aim(mousePos);
    }

    void updateWorld(double dt){
        for(PhysicsBody o : world)
            o.update(dt);
    }

    void renderFrame(Renderer ctx){
        for(PhysicsBody o : world)
            o.draw(ctx);
    }

    public void run(List<String> inputs, Vec2 mousePos, double dt) {
        //System.out.println("Events: " + inputs);
        if (isRunning){
            ctx.clearScreen(Color.BLACK);
            processEvents(inputs, mousePos);
            updateWorld(dt);
            renderFrame(ctx);
            ctx.updateScreen();
        }
    }

    public void CreateEntity(PhysicsBody pBody){
        Logger.LogInfo("GameManager spawning " + pBody.getClass().getSimpleName());
        world.add(pBody);
    }
    
    public void DestroyEntity(PhysicsBody pBody){
        Logger.LogInfo("GameManager destroying " + pBody.getClass().getSimpleName());
        world.remove(pBody);
    }
    
    public static GameManager getInstance(){
        return instance;
    }
}
