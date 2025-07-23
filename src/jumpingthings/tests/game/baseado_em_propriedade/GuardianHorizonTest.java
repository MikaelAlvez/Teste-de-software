package jumpingthings.tests.game.baseado_em_propriedade;

import jumpingthings.main.game.GuardianHorizon;

import static org.assertj.core.api.Assertions.assertThat;

import jumpingthings.main.game.MatchWithClusterAndGuardian;
import net.jqwik.api.Property;
import net.jqwik.api.ForAll;
import net.jqwik.api.constraints.IntRange;

public class GuardianHorizonTest {

    /** O total de moedas do guardião nunca deve ser negativo */
    @Property
    void guardianCoinsShouldNeverBeNegativeDuringSimulation(@ForAll @IntRange(min = 5, max = 50) int creatureCount) {
        MatchWithClusterAndGuardian match = new MatchWithClusterAndGuardian(creatureCount);

        // Itera múltiplas vezes para permitir interações do guardião com clusters
        for (int i = 0; i < 100; i++) {
            match.iterate();
            var guardian = match.getGuardian();
            int guardianCoins = guardian.getGameplay().getCoins();

            // Verifica que o guardião nunca ficou com moedas negativas
            assertThat(guardianCoins).isGreaterThanOrEqualTo(0);
        }
    }

}

