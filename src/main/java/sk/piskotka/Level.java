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

/**
 * The {@code Level} class represents the game level containing various game entities
 * and the player's ship. It manages the creation, deletion, updating, and rendering of
 * all objects within the level. The class follows the Observer design pattern to handle
 * the dynamic entities within the game world, including physics and collisions.
 * 
 * <p>The level includes functionalities such as creating and destroying entities, 
 * updating the state of all entities, handling collisions between physics bodies, 
 * and rendering the objects to the screen. The level can also print a hierarchy of 
 * transforms, useful for debugging and visualizing the structure of game objects.</p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Manages the game objects, including the player ship and other entities.</li>
 *     <li>Handles the creation and destruction of objects marked for creation or deletion.</li>
 *     <li>Processes physics updates and handles collisions between objects.</li>
 *     <li>Renders the level and all drawable objects to the screen.</li>
 *     <li>Can print a hierarchy of the game world transforms for debugging purposes.</li>
 * </ul>
 * 
 * @author Piskotka
 */
public class Level {
    
    /** The root transform of the level, serving as the parent for all other transforms. */
    private final Transform root;
    
    /** The player ship in the current level. */
    private PlayerShip player;
    
    /** List of all game objects in the level. */
    private List<Transform> objects;
    
    /** List of objects marked for deletion during the update cycle. */
    private final List<Transform> markedForDeletion;
    
    /** List of objects marked for creation during the update cycle. */
    private final List<Transform> markedForCreation;

    /**
     * Creates a new level, initializing empty lists for objects, marked-for-deletion,
     * and marked-for-creation entities. The root transform is also created for the level.
     */
    public Level() {
        Logger.logInfo(getClass(), "Creating new level");
        this.markedForDeletion = new ArrayList<>();
        this.markedForCreation = new ArrayList<>();
        this.objects = new LinkedList<>();
        this.root = Transform.createRoot();
    }

    /**
     * Adds a new entity to the level. If the entity is a {@link PlayerShip}, it is set as the player.
     * The entity is added to the list of objects marked for creation during the next update cycle.
     * 
     * @param pBody The entity to be created (can be any type of {@link Transform}).
     */
    public void create(Transform pBody) {
        if (pBody instanceof PlayerShip)
            setPlayer((PlayerShip)pBody);
        markedForCreation.add(pBody);
    }

    /**
     * Marks an entity for deletion. If the entity is a {@link PlayerShip}, an error is thrown
     * to prevent the player from being destroyed accidentally.
     * 
     * @param pBody The entity to be destroyed.
     */
    public void destroy(Transform pBody) {
        markedForDeletion.add(pBody);
        if (pBody instanceof PlayerShip)
            Logger.throwError(getClass(), "Player was destroyed from level");
    }

    /**
     * Processes and removes all entities marked for deletion during the update cycle.
     * Invokes the {@code onDeath} method for each entity and removes it from the level.
     */
    private void destroyMarked() {
        for(Transform p : markedForDeletion) {
            p.onDeath();
            objects.remove(p);
        }
        markedForDeletion.clear();
    }

    /**
     * Processes and adds all entities marked for creation during the update cycle.
     * Sets their parent to the root transform of the level.
     */
    private void createMarked() {
        for(Transform p : markedForCreation) {            
            objects.add(p);
            p.setParent(root);
        }
        markedForCreation.clear();
    }

    /**
     * Retrieves the player ship currently in the level.
     * 
     * @return The player ship object.
     */
    public PlayerShip getPlayer() {
        return player;
    }

    /**
     * Sets the player ship for the level. Throws an error if the player has already been set.
     * 
     * @param player The player ship to be set.
     */
    public void setPlayer(PlayerShip player) {
        if (this.player != null)
            Logger.throwError(getClass(), "Overwriting player ref because new player object was created!");
        this.player = player;
    }

    /**
     * Updates the level by updating all entities and handling collisions between physics bodies.
     * 
     * @param dt The delta time (time elapsed) between frames.
     */
    public void update(double dt) {
        for (Transform obj : objects)
            obj.update(dt); // Update physics and other properties
        
        // Check for collisions between pairs of physics bodies
        PhysicsBody A, B;
        List<PhysicsBody> pBodies = getPhysicsBodies();
        for (int a = 0; a < pBodies.size() - 1; a++) {
            A = pBodies.get(a);
            for (int b = a + 1; b < pBodies.size(); b++) {
                B = pBodies.get(b);
                A.handleCollisionWith(B);
            }
        }

        destroyMarked();
        createMarked();
    }

    /**
     * Renders the level to the screen, clearing the background and drawing all objects
     * that implement the {@link Drawable} interface.
     * 
     * @param ctx The renderer used to draw the level and its objects.
     */
    public void render(Renderer ctx) {
        ctx.clearBackground(Color.BLACK);
        for (Transform object : objects) {
            if (object instanceof Drawable drawable) {
                drawable.draw(ctx);
            }
        }
    }

    /**
     * Prints the hierarchy of transforms in the level, starting from the root transform.
     * 
     * @param transform The transform to start printing from.
     * @param depth The depth of the current transform in the hierarchy (used for indentation).
     * @return A string representation of the transform hierarchy.
     */
    private String printHierarchy(Transform transform, int depth) {
        String wholePrint = "\n" + " ".repeat(depth * 2) + "- " + (transform == root ? "root" : transform.toString()); 
        
        // Recursively print all children
        for (Transform child : transform.getChildren()) {
            wholePrint += printHierarchy(child, depth + 1);
        }
        return wholePrint;
    }

    /**
     * Prints the hierarchy of the level's transforms for debugging purposes.
     */
    public void printLevelHierarchy() {
        Logger.logInfo(getClass(), printHierarchy(root, 0));
    }

    /**
     * Retrieves a list of all {@link PhysicsBody} objects in the level.
     * 
     * @return A list of all physics bodies in the level.
     */
    private List<PhysicsBody> getPhysicsBodies() {
        List<PhysicsBody> pBodies = new ArrayList<>();
        for (Transform t : objects)
            if (t instanceof PhysicsBody physicsBody)
                pBodies.add(physicsBody);
        return pBodies;
    }
}
