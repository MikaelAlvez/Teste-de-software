package jumpingthings.tests.game.especificacao;

import jumpingthings.main.common.Env;
import jumpingthings.main.game.Creature;
import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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

    // === TESTE DE ESPECIFICAÇÃO ===
    /** Verifica se o painel é criado com o tamanho preferido correto com base na constante de ambiente. */
    @Test
    public void testPanelPreferredSizeMatchesEnv() {
        Dimension expected = new Dimension(800, 600); // Substituir conforme Env.WINDOW_WIDTH/HEIGHT
        assertThat(panel.getPreferredSize()).isEqualTo(expected);
    }
}

