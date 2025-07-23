package jumpingthings.tests.game.baseado_em_propriedade;

import jumpingthings.main.game.GuardianHorizon;

import static org.assertj.core.api.Assertions.assertThat;

import net.jqwik.api.Property;
import net.jqwik.api.ForAll;

public class GuardianHorizonTest {

    /** O total de moedas do guardião nunca deve ser negativo */
    @Property
    void guardianCoinsNeverNegative(@ForAll int id) {
        if (id > 1) {
            GuardianHorizon guardian = new GuardianHorizon(id);
            assertThat(guardian.getGameplay().getCoins()).isGreaterThanOrEqualTo(0);
        }
    }
}

