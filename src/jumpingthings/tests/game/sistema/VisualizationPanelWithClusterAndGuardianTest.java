package jumpingthings.tests.game.sistema;

import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class VisualizationPanelWithClusterAndGuardianTest {

    private MatchWithClusterAndGuardian match;
    private VisualizationPanelWithClusterAndGuardian panel;
    private JFrame frame;

    @BeforeEach
    void setup() {
        match = new MatchWithClusterAndGuardian(10); // Cria uma partida com 10 criaturas
        panel = new VisualizationPanelWithClusterAndGuardian(match);
        frame = new JFrame();
    }
    // === TESTES DE SISTEMA ===

    /**
     * Testa se o painel pode ser exibido em uma janela visível sem falhas.
     */
    @Test
    public void testFullPanelRenderingInWindow() {
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        panel.repaint();
        assertThat(frame.isVisible()).isTrue();
        frame.dispose();
    }

    /*Testar exibição visual em uma JFrame real*/
    @Test
    @Disabled("Teste visual manual")
    public void testVisualizationPanelRunsInFrame() {
        JFrame frame = new JFrame("Visual Test");
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        JOptionPane.showMessageDialog(frame, "Verifique visualmente o painel");
        frame.dispose();
    }

    /*Testar compatibilidade com redimensionamento da janela*/
    @Test
    public void testPanelResizeDoesNotCrash() {
        panel.setSize(1024, 768);
        assertDoesNotThrow(() -> panel.repaint());
    }
}

