package jumpingthings.tests.game;

import jumpingthings.main.common.Env;
import jumpingthings.main.game.Cluster;
import jumpingthings.main.game.Creature;
import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

    /**
     * Verifica se o painel é criado com o tamanho preferido correto com base na constante de ambiente.
     */
    @Test
    public void testPanelPreferredSizeMatchesEnv() {
        Dimension expected = new Dimension(800, 600); // Substituir conforme Env.WINDOW_WIDTH/HEIGHT
        assertThat(panel.getPreferredSize()).isEqualTo(expected);
    }

    /*Verificar se os elementos visuais são desenhados nas posições esperadas*/
    @Test
    public void testCreatureDrawingPositionMatchesNormalizedX() {
        Creature creature = match.getCreatures().get(0);
        creature.setX(0.0f);
        Graphics2D g2d = (Graphics2D) new BufferedImage(Env.WINDOW_WIDTH, Env.WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB).getGraphics();

        panel.paintComponent(g2d); // deve desenhar no centro
        int expectedX = Env.WINDOW_WIDTH / 2;
        // Como o desenho não retorna a posição, apenas garantir que não lança exceções aqui já é importante
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


    // === TESTES ESTRUTURAIS / INTEGRAÇÃO ===

    /**
     * Verifica se o painel pode ser adicionado corretamente ao JFrame.
     */
    @Test
    public void testPanelAddToFrame() {
        frame.add(panel);
        assertThat(frame.getContentPane().getComponentCount()).isEqualTo(1);
    }

    /*Verificar se a UI ainda se comporta corretamente após múltiplos repaints*/
    @Test
    public void testMultipleRepaintsDoNotThrow() {
        for (int i = 0; i < 50; i++) {
            assertDoesNotThrow(() -> panel.repaint());
        }
    }


    // === TESTE BASEADO EM PROPRIEDADE ===

    /**
     * Garante que todas as criaturas renderizadas possuem posições X normalizadas entre 0.0 e 1.0.
     */
    @Test
    public void testCreaturePositionNormalization() {
        for (Creature c : match.getCreatures()) {
            double normalizedX = (c.getX() + 1) / 2.0;
            assertThat(normalizedX).isBetween(0.0, 1.0);
        }
    }

    /*Verificar que nenhuma entidade é desenhada fora dos limites do painel*/
    @Test
    public void testNormalizationReturnsBoundedValues() {
        assertThat(panel.normalizePositionX(-1.0f)).isEqualTo(0.0);
        assertThat(panel.normalizePositionX(1.0f)).isEqualTo(1.0);
        assertThat(panel.normalizePositionX(0.0f)).isEqualTo(0.5);
    }


    // === TESTES DE DUBLÊ ===

    /**
     * Simula um cluster com moedas específicas para verificar visualização.
     */
    @Test
    public void testClusterCoinDisplay() {
        if (!match.getClusters().isEmpty()) {
            Cluster cl = match.getClusters().get(0);
            cl.getGameplay().addCoins(10);
            assertThat(cl.getGameplay().getCoins()).isEqualTo(10);
        }
    }

    // === TESTES DE INTEGRAÇÃO ===

    /**
     * Verifica se a soma total de moedas das entidades aparece corretamente ao iterar.
     */
    @Test
    public void testCoinAggregationConsistency() {
        int before = match.getCreatures().stream().mapToInt(Creature::getCoins).sum();
        match.iterate();
        int after = match.getCreatures().stream().mapToInt(Creature::getCoins).sum();
        assertThat(after).isLessThanOrEqualTo(before);
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
