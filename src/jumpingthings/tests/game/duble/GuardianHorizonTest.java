package jumpingthings.tests.game.duble;

import jumpingthings.main.game.GuardianHorizon;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GuardianHorizonTest {

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

