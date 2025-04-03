
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sk.piskotka.physics.Vec2;
import sk.piskotka.shapes.PolygonShape;
import sk.piskotka.ship.PlayerShip;


public class PlayerShipTest {
    PlayerShip playerShip;

    @BeforeEach
    void setUp() {
        playerShip = new PlayerShip(100, 200, 100, 150);
    }

    @Test
    void testInitialization() {
        assertNotNull(playerShip);
        assertEquals(new Vec2(100, 200), playerShip.getGlobalPos(), "PlayerShip should initialize at (100,200)");
        assertEquals(100, playerShip.getHealth(), "Initial health should be 100");
        assertEquals(150, playerShip.getMaxHealth(), "Max health should be 150");
        assertTrue(playerShip.getShape() instanceof PolygonShape, "PlayerShip should have a PolygonShape");
    }

    @Test
    void testWrongStatInit() {
        try{
            playerShip = new PlayerShip(0, 0, 0, 0);
        }catch (Exception e){
            assertEquals(e.getMessage(), "Component initiated with invalid data");
        }
        try{
            playerShip = new PlayerShip(0, 0, 10, 5);
        }catch (Exception e){
            assertEquals(e.getMessage(), "Component initiated with invalid data");
        }
    }

    @Test
    void testMovement() {
        List<Vec2> directions = new ArrayList<>();
        directions.add(Vec2.LEFT());
        directions.add(Vec2.RIGHT());
        directions.add(Vec2.UP());
        directions.add(Vec2.DOWN());
        for(Vec2 dir : directions){
            Vec2 startPos = playerShip.getGlobalPos();
            // Move left
            playerShip.move(dir);
            playerShip.update(1); // One second has passed
            Vec2 endPos = playerShip.getGlobalPos();
            
            if (dir.equals(Vec2.LEFT())){
                assertNotEquals(startPos, endPos);
                assertTrue(endPos.getX() < startPos.getX() && endPos.getY() == startPos.getY());
            }
            else if(dir.equals(Vec2.RIGHT())){
                assertNotEquals(startPos, endPos);
                assertTrue(endPos.getX() > startPos.getX() && endPos.getY() == startPos.getY());
            }
            else if(dir.equals(Vec2.UP())){
                assertNotEquals(startPos, endPos);
                assertTrue(endPos.getX() == startPos.getX() && endPos.getY() > startPos.getY());
            }
            else if(dir.equals(Vec2.DOWN())){
                assertNotEquals(startPos, endPos);
                assertTrue(endPos.getX() == startPos.getX() && endPos.getY() < startPos.getY());
            }
            else if(dir.equals(Vec2.ZERO()))
                assertEquals(endPos, startPos);
        }
    }

    @Test
    void testShipDestroyedAtZeroHealth() {
        playerShip.takeDamage(150);  // Reduce to 0 health

        assertEquals(0, playerShip.getHealth(), "Ship should be destroyed when health reaches 0");
    }

    @Test
    void testTakingDamage() {
        playerShip.takeDamage(30);

        assertEquals(70, playerShip.getHealth(), "Health should decrease to 70 after taking 30 damage");
    }

    // More will be added when guns will be added and ammo switching, picking up stuff etc

}
