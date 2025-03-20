package sk.piskotka;

import java.util.ArrayList;
import java.util.List;

import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.render.Renderer;
import sk.piskotka.ship.PlayerShip;

// Observer design pattern
public class Level {
    private PlayerShip player;
    private List<PhysicsBody> pBodies;

    public Level(){
        Logger.LogInfo(this, "Creating new level");
        this.pBodies = new ArrayList<>();
    }

    public void Create(PhysicsBody pBody){
        Logger.LogInfo(this, "Adding new entity typeof: " + pBody.getClass().getSimpleName());
        if (pBody instanceof PlayerShip)
            setPlayer((PlayerShip)pBody);
        
        pBodies.add(pBody);
    }

    public void Destroy(PhysicsBody pBody){
        Logger.LogInfo(this, "Destroying entity: " + pBody.getClass().getSimpleName());
        if (pBody instanceof PlayerShip)
            Logger.LogError(this, "Player was destroyed from level");
        pBodies.remove(pBody);
    }

    public PlayerShip getPlayer() {
        return player;
    }

    public void setPlayer(PlayerShip player) {
        if (this.player != null)
            Logger.LogError(this, "Overwriting player ref because new player object was created!");
        this.player = player;
    }

    public void update(double dt){
        for(PhysicsBody pb : pBodies)
            pb.update(dt);
    }

    public void render(Renderer ctx){
        for(PhysicsBody pb : pBodies)
            pb.draw(ctx);
    }
}
