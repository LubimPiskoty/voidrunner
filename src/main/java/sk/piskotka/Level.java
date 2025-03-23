package sk.piskotka;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.paint.Color;
import sk.piskotka.logger.Logger;
import sk.piskotka.physics.Collider;
import sk.piskotka.physics.Collider.CollisionResult;
import sk.piskotka.physics.PhysicsBody;
import sk.piskotka.physics.Transform;
import sk.piskotka.physics.Vec2;
import sk.piskotka.projectile.Projectile;
import sk.piskotka.render.Renderer;
import sk.piskotka.ship.PlayerShip;

// Observer design pattern
public class Level {
    private Transform root;
    private PlayerShip player;
    private List<PhysicsBody> pBodies;
    private List<PhysicsBody> markedForDeletion;
    private int resolutionSteps;

    public Level(){
        Logger.logInfo(getClass(), "Creating new level");
        this.markedForDeletion = new ArrayList<>();
        this.pBodies = new LinkedList<>();
        this.root = Transform.createRoot();
        this.resolutionSteps = 4; // Bigger number makes simulation more stable
    }

    public void create(PhysicsBody pBody){
        Logger.logInfo(getClass(), "Adding new entity typeof: " + pBody.toString());
        if (pBody instanceof PlayerShip)
            setPlayer((PlayerShip)pBody);
        
        pBodies.add(pBody);
        pBody.setParent(root);
    }
    
    public void create(PhysicsBody pBody, Transform parent){
        create(pBody);
        pBody.setParent(parent);
    }

    public void destroy(PhysicsBody pBody){
        Logger.logInfo(getClass(), "Destroying entity: " + pBody.toString());
        if (pBody instanceof PlayerShip)
            Logger.logError(getClass(), "Player was destroyed from level");
        markedForDeletion.add(pBody);
    }

    private void destroyMarked(){
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
            pb.update(dt); // Update physics etc

        destroyMarked();

        //TODO: This will need to be refactored
        // Check for collision
        for(int i = 0; i < this.resolutionSteps; i++){
            // Check each combination of pBodies
            PhysicsBody A, B;
            for(int a = 0; a < pBodies.size()-1; a++){
                A = pBodies.get(a);
     
                for(int b = a+1; b < pBodies.size(); b++){
                    B = pBodies.get(b);

                    if(A.checkCollisionWith(B)){
                        //Logger.logDebug(getClass(), "update.resolutionStep: " + A + " is colliding with " + B);
                        //GameManager.getRenderer().drawVector(cResult.point, cResult.normal.multiply(cResult.penetration), Color.LIME);
                    }
                }
            }
        }
    }

    public void render(Renderer ctx){
        for(PhysicsBody pb : pBodies){
            pb.draw(ctx);

            if (GameManager.getInstance().isDebug()){
                pb.getCollider().draw(ctx);
            }
        }
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
