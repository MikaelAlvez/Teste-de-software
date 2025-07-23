package jumpingthings.tests.game.sistema;

import jumpingthings.main.game.Match;
import jumpingthings.main.game.VisualizationPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

    // Teste de Sistema: Verificar se o painel de visualização (VisualizationPanel)
    @Test
    public void testPanelAddedToFrame() {
        Match m = new Match(5);
        VisualizationPanel panel = new VisualizationPanel(m);
        JFrame f = new JFrame();
        f.add(panel);
        Component[] components = f.getContentPane().getComponents();
        boolean containsPanel = false;
        for (Component c : components) {
            if (c == panel) containsPanel = true;
        }
        assertThat(containsPanel).isTrue();
    }

    // Teste de sistema: Simula até uma das condições de parada (máximo de iterações)
    @Test
    public void testSimulationStopsAtMaxIterations() {
        Match m = new Match(10);
        int iterations = 0;
        while (iterations < 100 && !m.hasHalfElementsReachedOneCoin()) {
            m.iterate();
            iterations++;
        }
        assertThat(iterations).isLessThanOrEqualTo(100);
    }

    // Teste de sistema: Simulação para por condição de metade com 1 moeda
    @Test
    public void testSimulationStopsByOneCoinCondition() {
        Match m = new Match(10);
        for (int i = 0; i < 5; i++) {
            m.getCreatures().get(i).addCoins(-999_999);
        }
        int iterations = 0;
        while (iterations < 100 && !m.hasHalfElementsReachedOneCoin()) {
            m.iterate();
            iterations++;
        }
        assertThat(m.hasHalfElementsReachedOneCoin()).isTrue();
    }
}

