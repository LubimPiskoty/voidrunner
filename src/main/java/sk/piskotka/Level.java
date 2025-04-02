package sk.piskotka;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.paint.Color;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Transform;
import sk.piskotka.render.Drawable;
import sk.piskotka.render.Renderer;
import sk.piskotka.ship.PlayerShip;

// Observer design pattern
public class Level {
    private Transform root;
    private PlayerShip player;
    private List<Transform> objects;
    private List<Transform> markedForDeletion;
    private List<Transform> markedForCreation;

    public Level(){
        Logger.logInfo(getClass(), "Creating new level");
        this.markedForDeletion = new ArrayList<>();
        this.markedForCreation = new ArrayList<>();
        this.objects = new LinkedList<>();
        this.root = Transform.createRoot();
    }

    public void create(Transform pBody){
        //Logger.logInfo(getClass(), "Adding new entity typeof: " + pBody.toString());
        if (pBody instanceof PlayerShip)
            setPlayer((PlayerShip)pBody);
        markedForCreation.add(pBody);
    }

    public void destroy(Transform pBody){
        //Logger.logInfo(getClass(), "Destroying entity: " + pBody.toString());
        markedForDeletion.add(pBody);
        if (pBody instanceof PlayerShip)
            Logger.throwError(getClass(), "Player was destroyed from level");
    }

    private void destroyMarked(){
        for(Transform p : markedForDeletion){
            p.onDeath();
            objects.remove(p);
        }
        markedForDeletion.clear();
    }

    private void createMarked(){
        for(Transform p : markedForCreation){            
            objects.add(p);
            p.setParent(root);
        }
        markedForCreation.clear();
    }

    public PlayerShip getPlayer() {
        return player;
    }

    public void setPlayer(PlayerShip player) {
        if (this.player != null)
            Logger.throwError(getClass(), "Overwriting player ref because new player object was created!");
        this.player = player;
    }

    public void update(double dt){
        for(Transform obj : objects)
        obj.update(dt); // Update physics etc
        
        
        // Check each combination of objects
        PhysicsBody A, B;
        List<PhysicsBody> pBodies = getPhysicsBodies();
        for(int a = 0; a < pBodies.size()-1; a++){
            A = pBodies.get(a);
    
            for(int b = a+1; b < pBodies.size(); b++){
                B = pBodies.get(b);
                A.handleCollisionWith(B);
            }
        }
        destroyMarked();
        createMarked();
        
    }
    
    public void render(Renderer ctx){
        ctx.clearBackground(Color.BLACK);
        // Create background paralax

        for(Transform object : objects)
            if (object instanceof Drawable)
                ((Drawable)object).draw(ctx);
    }

    private String printHierarchy(Transform transform, int depth){
        // Print current transform with indentation
        
        String wholePrint = "\n" + " ".repeat(depth * 2) + "- " + (transform == root ? "root" : transform.toString()); 
        
        // Recursively print all children
        for (Transform child : transform.getChildren()) {
            wholePrint += printHierarchy(child, depth + 1);
        }
        return wholePrint;
    }

    public void printLevelHierarchy(){
        Logger.logInfo(getClass(), printHierarchy(root, 0));
    }

    private List<PhysicsBody> getPhysicsBodies(){
        List<PhysicsBody> pBodies = new ArrayList<>();
        for(Transform t : objects)
            if (t instanceof PhysicsBody)
                pBodies.add((PhysicsBody)t);
        return pBodies;
    }
}
