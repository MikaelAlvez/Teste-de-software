package jumpingthings.tests.game.baseado_em_propriedade;

import jumpingthings.main.game.Creature;
import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;

import org.junit.jupiter.api.BeforeEach;

import javax.swing.*;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Positive;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchWithClusterAndGuardianTest {

    private MatchWithClusterAndGuardian match;
    private VisualizationPanelWithClusterAndGuardian panel;
    private JFrame frame;

    @BeforeEach
    void setup() {
        match = new MatchWithClusterAndGuardian(10);
        panel = new VisualizationPanelWithClusterAndGuardian(match);
        frame = new JFrame();
    }

    /** Verifica que o total de moedas não aumenta após iteração */
    /** Propriedade: O total de moedas não aumenta após qualquer iteração */
    @Property
    void totalCoinsShouldNotIncrease(@ForAll @IntRange(min = 2, max = 100) int creatureCount) {
        MatchWithClusterAndGuardian match = new MatchWithClusterAndGuardian(creatureCount);
        int totalAntes = match.getCreatures().stream().mapToInt(Creature::getCoins).sum();
        match.iterate();
        int totalDepois = match.getCreatures().stream().mapToInt(Creature::getCoins).sum();
        assertThat(totalDepois).isLessThanOrEqualTo(totalAntes);
    }

    /** Propriedade: Todas as posições X devem permanecer dentro dos limites após iteração */
    @Property
    void allCreaturePositionsWithinExpectedBounds(@ForAll @IntRange(min = 2, max = 100) int creatureCount) {
        MatchWithClusterAndGuardian match = new MatchWithClusterAndGuardian(creatureCount);
        match.iterate();
        List<Creature> creatures = match.getCreatures();
        for (Creature c : creatures) {
            assertThat(c.getX()).isBetween(-1.0F, 1.0F); // após 1 iteração
        }
    }
}
