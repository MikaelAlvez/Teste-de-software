package jumpingthings.tests.game.duble;


import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;

import org.assertj.core.api.AssertionsForClassTypes;
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
    
    // --- TESTES DE DUBLÊ ---

    /** Usa criaturas manipuladas para testar condição de parada */
    @Test
    public void testSimulationStopsWhenHalfReachOneCoin() {
        MatchWithClusterAndGuardian m = new MatchWithClusterAndGuardian(10);
        for (int i = 0; i < 5; i++) {
            m.getCreatures().get(i).addCoins(-m.getCreatures().get(i).getCoins());
            m.getCreatures().get(i).addCoins(1);
        }
        int iterations = 0;
        while (iterations < 100 && !m.hasHalfElementsReachedOneCoin()) {
            m.iterate();
            iterations++;
        }
        AssertionsForClassTypes.assertThat(m.hasHalfElementsReachedOneCoin()).isTrue();
        assertThat(iterations).isLessThanOrEqualTo(100);
    }
}

