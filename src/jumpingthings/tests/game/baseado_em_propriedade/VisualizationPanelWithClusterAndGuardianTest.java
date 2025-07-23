package jumpingthings.tests.game.baseado_em_propriedade;

import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;
import net.jqwik.api.constraints.FloatRange;
import org.junit.jupiter.api.BeforeEach;
import javax.swing.*;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class VisualizationPanelWithClusterAndGuardianTest {

    private MatchWithClusterAndGuardian match;
    private VisualizationPanelWithClusterAndGuardian panel;
    private JFrame frame;

    /** Garante que todas as criaturas renderizadas possuem posições X normalizadas entre 0.0 e 1.0. */
    @Property
    void normalizedXIsAlwaysBetweenZeroAndOne(@ForAll @FloatRange(min = -1.0f, max = 1.0f) float x) {
        double normalizedX = (x + 1) / 2.0;
        assertThat(normalizedX).isBetween(0.0, 1.0);
    }
}

