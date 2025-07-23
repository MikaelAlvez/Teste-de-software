package jumpingthings.tests.game.dominio_fronteira;

import jumpingthings.main.game.Cluster;
import jumpingthings.main.game.Creature;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ClusterTest {

    /** Teste de domínio e estrutural: Garante que o cluster é criado corretamente com o ID certo, total de moedas das criaturas e posição média X inicial.*/
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

    /** Teste de domínio: Verifica que valores de ID abaixo do mínimo permitido disparam exceção com mensagem esperada.*/
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

    /** Teste de domínio: Garante que um cluster com lista vazia de criaturas gera exceção*/
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

    /** Teste de domínio e estrutural: Testa um caso simples válido do domínio: cluster com apenas uma criatura. Valida cálculo de moedas e posição*/
    @Test
    void testClusterWithSingleCreature() {
        Creature c = new Creature(33, 150, 12);
        Cluster cluster = new Cluster(70, List.of(c));

        assertEquals(70, cluster.getId());
        assertEquals(12, cluster.getGameplay().getCoins());
        assertEquals(0.0, cluster.getGameplay().getX());
    }
}

