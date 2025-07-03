package jumpingthings.tests.game;


import jumpingthings.main.game.Cluster;
import jumpingthings.main.game.Creature;
import jumpingthings.main.game.GuardianHorizon;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GuardianHorizonTest {

  @Test
  void testValidGuardianCreation() {
    GuardianHorizon guardian = new GuardianHorizon(10);
    assertEquals(10, guardian.getId());
    assertEquals(0, guardian.getGameplay().getCoins());
  }

  @Test
  void testGuardianCreationInvalidId() {
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
      new GuardianHorizon(1);
    });
    assertEquals("Guardião precisa de um id maior que 1", ex.getMessage());
  }

  @Test
  void testAddClusterNullThrows() {
    GuardianHorizon guardian = new GuardianHorizon(10);

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
      guardian.addCluster(null);
    });

    assertEquals("Cluster não pode ser nulo!", ex.getMessage());
  }

  @Test
  void testAddClusterAddsCoins() {
    GuardianHorizon guardian = new GuardianHorizon(10);

    Creature c1 = new Creature(1, 50, 10);
    Creature c2 = new Creature(2, 70, 20);
    Cluster cluster = new Cluster(50, List.of(c1, c2)); // Total coins = 30

    guardian.addCluster(cluster);

    assertEquals(30, guardian.getGameplay().getCoins());
  }
}
