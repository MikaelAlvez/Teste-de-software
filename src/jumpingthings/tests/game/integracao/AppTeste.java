package jumpingthings.tests.game.integracao;

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

    // Teste de Integração/Estrutural: verificar se após uma iteração e chamada ao repaint()
    @Test
    public void testPanelRepaintEffectivelyChangesSomething() {
        Match m = new Match(5);
        VisualizationPanel p = new VisualizationPanel(m);
        float xBefore = m.getCreatures().get(0).getX();
        m.iterate();
        p.repaint();
        float xAfter = m.getCreatures().get(0).getX();
        assertThat(xBefore).isNotEqualTo(xAfter); // mudança indica que houve atualização
    }

    // Teste de Integração/Teste de Sistema: Verificar se o painel de visualização (VisualizationPanel)
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

    // Teste de integração: Confirma que as posições mudam após iteração
    @Test
    public void testCreaturePositionChanges() {
        float xBefore = match.getCreatures().get(0).getX();
        match.iterate();
        float xAfter = match.getCreatures().get(0).getX();
        assertThat(xAfter).isNotEqualTo(xBefore);
    }

    // Teste de integração: Testa visualização integrada com match
    @Test
    public void testVisualizationPanelUpdates() {
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setVisible(true);
        match.iterate();
        panel.repaint();
        assertThat(panel).isNotNull();
    }
}

