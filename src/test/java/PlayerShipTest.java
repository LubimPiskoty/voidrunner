
import org.junit.jupiter.api.Test;

import sk.piskotka.physics.Vec2;
import sk.piskotka.ship.PlayerShip;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;


public class PlayerShipTest {
    @Test
    @DisplayName("Test player ship creation")
    public void TestCreation(){
        PlayerShip player = new PlayerShip(0, 0);
        Assertions.assertNotEquals(player.getLocalPos(), Vec2.ZERO());
    }
}
