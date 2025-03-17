package sk.piskotka;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import sk.piskotka.Physics.GameObject;
import sk.piskotka.Physics.Vec2;
import sk.piskotka.render.Renderer;
import sk.piskotka.ship.PlayerShip;

public class GameManager {
    public static boolean isRunning = true;
    public double targetFrametime;
    private List<GameObject> world;
    private Renderer ctx;

    public GameManager(Renderer ctx) {
        this.targetFrametime = 0.016;
        this.ctx = ctx;
        System.out.println("Game is starting");
        world = new ArrayList<>();
        world.add(new PlayerShip());
    }

    void processEvents(List<String> inputs){
        PlayerShip p = ((PlayerShip)world.get(0));
        Vec2 inputVec = Vec2.ZERO();
        if (inputs.contains("W"))
            inputVec.add(Vec2.DOWN());
        if (inputs.contains("S"))
            inputVec.add(Vec2.UP());
        if (inputs.contains("A"))
            inputVec.add(Vec2.LEFT());
        if (inputs.contains("D"))
            inputVec.add(Vec2.RIGHT());
        
        p.move(inputVec);
    }

    void updateWorld(double dt){
        for(GameObject o : world)
            o.update(dt);
    }

    void renderFrame(Renderer ctx){
        for(GameObject o : world)
            o.draw(ctx);
    }

    public void run(List<String> inputs, double dt) {
        //System.out.println("Events: " + inputs);
        if (isRunning){
            ctx.clearScreen(Color.BLACK);
            processEvents(inputs);
            updateWorld(dt);
            renderFrame(ctx);
        }
    }
    
}
