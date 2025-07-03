package jumpingthings.tests.game;

import jumpingthings.main.game.Cluster;
import jumpingthings.main.game.Creature;
import jumpingthings.main.game.DefaultGameplay;
import jumpingthings.main.game.GuardianHorizon;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GuardianHorizonTest {

    // --- TESTES DE ESPECIFICAÇÃO ---

    /** Garante que o Guardião não aceita ID <= 1 (regras de negócio) */
    @Test
    public void testGuardianIdLowerBound() {
        assertThatThrownBy(() -> new GuardianHorizon(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Guardião precisa de um id maior que 1");
    }

    /** Garante que o Guardião aceita ID válido (>1) */
    @Test
    public void testGuardianIdValid() {
        GuardianHorizon guardian = new GuardianHorizon(2);
        assertThat(guardian.getId()).isEqualTo(2);
    }


    // --- TESTES DE FRONTEIRA ---

    /** Testa ID logo acima do limite mínimo (2) */
    @Test
    public void testGuardianIdBoundary() {
        GuardianHorizon guardian = new GuardianHorizon(2);
        assertThat(guardian.getId()).isEqualTo(2);
    }


    // --- TESTES ESTRUTURAIS / INTEGRAÇÃO ---

    /** Testa se adicionar um Cluster válido aumenta as moedas do Guardião */
    /*@Test
    public void testAddClusterIncreasesCoins() {
        GuardianHorizon guardian = new GuardianHorizon(10);
        Creature creature = new Creature(50);
        creature.addCoins(5);

        // Cluster com ID válido entre 41 e 99
        Cluster cluster = new Cluster(41, List.of(creature));

        guardian.addCluster(cluster);

        assertThat(guardian.getGameplay().getCoins()).isEqualTo(5);
    }*/


    // --- TESTES BASEADOS EM PROPRIEDADE ---

    /** O total de moedas do guardião nunca deve ser negativo */
    @Test
    public void testGuardianCoinsNonNegative() {
        GuardianHorizon guardian = new GuardianHorizon(10);
        assertThat(guardian.getGameplay().getCoins()).isGreaterThanOrEqualTo(0);
    }


    // --- TESTES DE DUBLÊS ---

    /** Tenta adicionar cluster nulo e espera exceção */
    @Test
    public void testAddClusterNullThrowsException() {
        GuardianHorizon guardian = new GuardianHorizon(10);
        assertThatThrownBy(() -> guardian.addCluster(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cluster não pode ser nulo");
    }


    // --- TESTES DE SISTEMA ---

    /** Testa integração total: criar Guardian, adicionar cluster com criaturas e validar moedas */
    /*@Test
    public void testFullIntegration() {
        GuardianHorizon guardian = new GuardianHorizon(10);

        Creature c1 = new Creature(45);
        c1.addCoins(3);
        Creature c2 = new Creature(46);
        c2.addCoins(2);

        Cluster cluster = new Cluster(50, List.of(c1, c2));

        guardian.addCluster(cluster);

        assertThat(guardian.getGameplay().getCoins()).isEqualTo(5);
    }*/
}
