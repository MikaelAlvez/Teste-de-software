package jumpingthings.tests.game.baseado_em_propriedade;

import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
    @Test
    public void testTotalCoinsNotIncreasing() {
        int totalAntes = match.getCreatures().stream().mapToInt(c -> c.getCoins()).sum();
        match.iterate();
        int totalDepois = match.getCreatures().stream().mapToInt(c -> c.getCoins()).sum();
        assertThat(totalDepois).isLessThanOrEqualTo(totalAntes);
    }

    /** Garante que posições X das criaturas estão dentro do intervalo esperado após iteração */
    @Test
    public void testPositionsWithinBounds() {
        match.iterate();
        for (var c : match.getCreatures()) {
            assertThat(c.getX()).isBetween(-1.0f, 1.0f);
        }
    }
}
