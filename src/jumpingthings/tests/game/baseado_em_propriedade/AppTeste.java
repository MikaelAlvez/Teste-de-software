package jumpingthings.tests.game.baseado_em_propriedade;

import jumpingthings.main.game.Match;
import jumpingthings.main.game.VisualizationPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class AppTeste {
    private Match match;
    private VisualizationPanel panel;
    private JFrame frame;

    @BeforeEach
    void setup() {
        match = new Match(10);
        panel = new VisualizationPanel(match);
        frame = new JFrame();
    }

    /** Teste baseado em propriedade: A quantidade total de moedas não aumenta */
    @Test
    public void testTotalCoinsPreservedOrDecreased() {
        int totalAntes = match.getCreatures().stream().mapToInt(c -> c.getCoins()).sum();
        match.iterate();
        int totalDepois = match.getCreatures().stream().mapToInt(c -> c.getCoins()).sum();
        assertThat(totalDepois).isLessThanOrEqualTo(totalAntes);
    }

    /** Teste baseado em propriedade: X deve estar entre -1.0 e 1.0 após iteração */
    @Test
    public void testPositionRemainsInBounds() {
        match.iterate();
        for (var c : match.getCreatures()) {
            assertThat(c.getX()).isBetween(-1.0f, 1.0f);
        }
    }
}

