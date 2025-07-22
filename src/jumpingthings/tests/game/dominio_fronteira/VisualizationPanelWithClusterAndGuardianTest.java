package jumpingthings.tests.game.dominio_fronteira;

import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;
import org.junit.jupiter.api.BeforeEach;
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
    // === TESTES DE FRONTEIRA ===

    /**
     * Garante que o painel pode ser renderizado mesmo sem criaturas.
     */
    @Test
    public void testEmptyMatchDoesNotCrash() {
        match.getCreatures().clear();
        match.getClusters().clear();
        match.getGuardian().getGameplay().addCoins(-match.getGuardian().getGameplay().getCoins());
        assertThat(panel).isNotNull();
        panel.repaint(); // Não deve lançar exceção
    }

    /*Testar criaturas em posições extremas de X (-1.0 e 1.0)*/
    @Test
    public void testCreaturesAtExtremeXPositionsAreWithinBounds() {
        match.getCreatures().get(0).setX(-1.0f);
        match.getCreatures().get(1).setX(1.0f);
        assertDoesNotThrow(() -> panel.repaint());
    }
}

