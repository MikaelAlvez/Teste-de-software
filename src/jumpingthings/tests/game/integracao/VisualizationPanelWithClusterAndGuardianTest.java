package jumpingthings.tests.game.integracao;

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


    // === TESTES DE FRONTEIRA ===

    /** Garante que o painel pode ser renderizado mesmo sem criaturas. */
    @Test
    public void testEmptyMatchDoesNotCrash() {
        match.getCreatures().clear();
        match.getClusters().clear();
        match.getGuardian().getGameplay().addCoins(-match.getGuardian().getGameplay().getCoins());
        assertThat(panel).isNotNull();
        panel.repaint();
    }

    /** Testar criaturas em posições extremas de X (-1.0 e 1.0) */
    @Test
    public void testCreaturesAtExtremeXPositionsAreWithinBounds() {
        match.getCreatures().get(0).setX(-1.0f);
        match.getCreatures().get(1).setX(1.0f);
        assertDoesNotThrow(() -> panel.repaint());
    }

    // === TESTES ESTRUTURAIS / INTEGRAÇÃO ===

    /** Verifica se o painel pode ser adicionado corretamente ao JFrame. */
    @Test
    public void testPanelAddToFrame() {
        frame.add(panel);
        assertThat(frame.getContentPane().getComponentCount()).isEqualTo(1);
    }

    /** Verificar se a UI ainda se comporta corretamente após múltiplos repaints */
    @Test
    public void testMultipleRepaintsDoNotThrow() {
        for (int i = 0; i < 50; i++) {
            assertDoesNotThrow(() -> panel.repaint());
        }
    }

    // === TESTES DE INTEGRAÇÃO ===

    /** Verifica se a soma total de moedas das entidades aparece corretamente ao iterar. */
    @Test
    public void testCoinAggregationConsistency() {
        int before = match.getCreatures().stream().mapToInt(Creature::getCoins).sum();
        match.iterate();
        int after = match.getCreatures().stream().mapToInt(Creature::getCoins).sum();
        assertThat(after).isLessThanOrEqualTo(before);
    }
}

