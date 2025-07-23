package jumpingthings.tests.game.especificacao;

import jumpingthings.main.game.GuardianHorizon;

import org.junit.jupiter.api.Test;

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
}

