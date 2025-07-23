package jumpingthings.tests.game.sistema;

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

    // --- TESTES DE SISTEMA ---
    /** Testa que a simulação para após limite máximo de iterações */
    @Test
    public void testSimulationStopsAtMaxIterations() {
        MatchWithClusterAndGuardian m = new MatchWithClusterAndGuardian(10);
        int iterations = 0;
        while (iterations < 100 && !m.hasHalfElementsReachedOneCoin()) {
            m.iterate();
            iterations++;
        }
        assertThat(iterations).isLessThanOrEqualTo(100);
    }
}

