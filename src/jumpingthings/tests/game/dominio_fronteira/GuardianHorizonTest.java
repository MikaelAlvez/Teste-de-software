package jumpingthings.tests.game.dominio_fronteira;

import jumpingthings.main.game.GuardianHorizon;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import jumpingthings.main.game.Cluster;
import jumpingthings.main.game.Creature;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GuardianHorizonTest {
    // --- TESTES DE FRONTEIRA ---

    /** Testa ID logo acima do limite mínimo (2) */
    @Test
    public void testGuardianIdBoundary() {
        GuardianHorizon guardian = new GuardianHorizon(2);
        assertThat(guardian.getId()).isEqualTo(2);
    }

    /** Testa a validação de entrada e o lançamento de exceção com mensagem esperada */
    @Test
    void testGuardianCreationInvalidId() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new GuardianHorizon(1);
        });
        assertEquals("Guardião precisa de um id maior que 1", ex.getMessage());
    }
    // --- TESTES DE DOMINIO E ESTRUTURAL ---

    /** Testa a lógica de soma de moedas (comportamento interno) */
    @Test
    void testAddClusterAddsCoins() {
        GuardianHorizon guardian = new GuardianHorizon(10);

        Creature c1 = new Creature(1, 50, 10);
        Creature c2 = new Creature(2, 70, 20);
        Cluster cluster = new Cluster(50, List.of(c1, c2)); // Total coins = 30

        guardian.addCluster(cluster);

        assertEquals(30, guardian.getGameplay().getCoins());
    }
    /** Verifica atributos após construção do objeto */
    @Test
    void testValidGuardianCreation() {
        GuardianHorizon guardian = new GuardianHorizon(10);
        assertEquals(10, guardian.getId());
        assertEquals(0, guardian.getGameplay().getCoins());
    }
}

