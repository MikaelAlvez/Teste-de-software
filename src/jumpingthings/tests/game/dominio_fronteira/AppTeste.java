package jumpingthings.tests.game.dominio_fronteira;

import jumpingthings.main.game.Match;
import jumpingthings.main.game.VisualizationPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

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

    /** Teste de fronteira: Criação de match com zero criaturas deve lançar exceção */
    @Test
    public void testMatchComZeroCriaturas() {
        assertThatThrownBy(() -> new Match(0))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Número de criaturas insuficientes");
    }

    /** Teste de fronteira: Criação com número máximo (30 criaturas) */
    @Test
    public void testMatchMaxLimit() {
        Match largeMatch = new Match(100);
        assertThat(largeMatch.getCreatures()).hasSize(30);
    }

    /** Estrutural: verificar se após uma iteração e chamada ao repaint() */
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
}

