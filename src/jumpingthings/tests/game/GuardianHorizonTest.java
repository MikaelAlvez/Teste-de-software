package jumpingthings.tests.game;

import jumpingthings.main.game.Cluster;
import jumpingthings.main.game.Creature;
import jumpingthings.main.game.DefaultGameplay;
import jumpingthings.main.game.GuardianHorizon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.jqwik.api.Property;
import net.jqwik.api.ForAll;

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

    // --- TESTES BASEADOS EM PROPRIEDADE ---

    /** O total de moedas do guardião nunca deve ser negativo */
    @Property
    void guardianCoinsNeverNegative(@ForAll int id) {
        if (id > 1) {
            GuardianHorizon guardian = new GuardianHorizon(id);
            assertThat(guardian.getGameplay().getCoins()).isGreaterThanOrEqualTo(0);
        }
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
}
