package sk.piskotka;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Transform;
import sk.piskotka.render.Renderer;
import sk.piskotka.ship.PlayerShip;

// Observer design pattern
public class Level {
    private Transform root;
    private PlayerShip player;
    private List<PhysicsBody> pBodies;
    private List<PhysicsBody> markedForDeletion;

    public Level(){
        Logger.logInfo(getClass(), "Creating new level");
        this.markedForDeletion = new ArrayList<>();
        this.pBodies = new LinkedList<>();
        this.root = Transform.createRoot();
    }

    public void Create(PhysicsBody pBody){
        Logger.logInfo(getClass(), "Adding new entity typeof: " + pBody.toString());
        if (pBody instanceof PlayerShip)
            setPlayer((PlayerShip)pBody);
        
        pBodies.add(pBody);
        pBody.setParent(root);
    }
    
    public void Create(PhysicsBody pBody, Transform parent){
        Create(pBody);
        pBody.setParent(parent);
    }

    public void Destroy(PhysicsBody pBody){
        Logger.logInfo(getClass(), "Destroying entity: " + pBody.toString());
        if (pBody instanceof PlayerShip)
            Logger.logError(getClass(), "Player was destroyed from level");
        markedForDeletion.add(pBody);
    }

    public void DestroyMarked(){
        for(PhysicsBody p : markedForDeletion){
            p.getParent().removeChild(p);
            pBodies.remove(p);
        }
        markedForDeletion.clear();
    }

    public PlayerShip getPlayer() {
        return player;
    }

    public void setPlayer(PlayerShip player) {
        if (this.player != null)
            Logger.logError(getClass(), "Overwriting player ref because new player object was created!");
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
    
    private String printHierarchy(Transform transform, int depth){
        // Print current transform with indentation
        
        String wholePrint = "\n" + " ".repeat(depth * 2) + "- " + transform.toString(); 
        
        // Recursively print all children
        for (Transform child : transform.getChildren()) {
            wholePrint += printHierarchy(child, depth + 1);
        }
        return wholePrint;
    }

    public void printLevelHierarchy(){
        Logger.logDebug(getClass(), printHierarchy(root, 0));
    }
}
