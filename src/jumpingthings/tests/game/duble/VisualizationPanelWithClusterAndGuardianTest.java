package jumpingthings.tests.game.duble;

import jumpingthings.main.game.Cluster;
import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.assertj.core.api.Assertions.assertThat;

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

    // === TESTES DE DUBLÊ ===

    /** Simula um cluster com moedas específicas para verificar visualização. */
    @Test
    public void testClusterCoinDisplay() {
        if (!match.getClusters().isEmpty()) {
            Cluster cl = match.getClusters().get(0);
            cl.getGameplay().addCoins(10);
            assertThat(cl.getGameplay().getCoins()).isEqualTo(10);
        }
    }
}
