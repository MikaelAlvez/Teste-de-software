package jumpingthings.tests.game.especificacao;

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

    // Teste de especificação: Garante que a simulação começa com o número correto de criaturas
    @Test
    public void testMatchInitialization() {
        assertThat(match.getCreatures()).hasSize(10);
    }
}

