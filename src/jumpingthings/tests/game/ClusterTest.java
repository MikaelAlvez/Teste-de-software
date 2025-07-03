package jumpingthings.tests.game;

import jumpingthings.main.game.Cluster;
import jumpingthings.main.game.Creature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ClusterTest {

  @Test
  void testValidClusterCreation() {
    Creature c1 = new Creature(50, 100, 10);
    Creature c2 = new Creature(70, 200, 15);
    List<Creature> creatures = List.of(c1, c2);

    Cluster cluster = new Cluster(50, creatures);

    assertEquals(50, cluster.getId());
    assertEquals(25, cluster.getGameplay().getCoins());
    assertEquals(0.0, cluster.getGameplay().getX());
  }

  @Test
  void testInvalidClusterIdTooLow() {
    Creature c = new Creature(30, 0, 5);
    try {
      new Cluster(40, List.of(c));
      fail("Deveria ter lançado IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
      assertEquals("Faixa de id inválida!", ex.getMessage());
    }
  }

  @Test
  void testInvalidClusterIdTooHigh() {
    Creature c = new Creature(30, 0, 5);
    try {
      new Cluster(100, List.of(c));
      fail("Deveria ter lançado IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
      assertEquals("Faixa de id inválida!", ex.getMessage());
    }
  }

  @Test
  void testCreaturesIsNull() {
    Creature c = new Creature(30, 0, 5);
    try {
      new Cluster(41, List.of());
      fail("Deveria ter lançado IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
      assertEquals("Criaturas não podem ser vazias!", ex.getMessage());
    }
  }

  @Test
  void testClusterWithSingleCreature() {
    Creature c = new Creature(33, 150, 12);
    Cluster cluster = new Cluster(70, List.of(c));

    assertEquals(70, cluster.getId());
    assertEquals(12, cluster.getGameplay().getCoins());
    assertEquals(0.0, cluster.getGameplay().getX());
  }
}
