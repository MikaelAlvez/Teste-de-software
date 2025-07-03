package jumpingthings.tests.game;

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

    // Teste de fronteira: Criação de match com zero criaturas deve lançar exceção
    @Test
    public void testMatchComZeroCriaturas() {
        assertThatThrownBy(() -> new Match(0))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Número de criaturas insuficientes");
    }

    // Teste de fronteira: Criação com número máximo (30 criaturas)
    @Test
    public void testMatchMaxLimit() {
        Match largeMatch = new Match(100);
        assertThat(largeMatch.getCreatures()).hasSize(30);
    }

    // Teste de Integração / Estrutural: verificar se após uma iteração e chamada ao repaint()
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

    // Teste baseado em propriedade: A quantidade total de moedas não aumenta
    @Test
    public void testTotalCoinsPreservedOrDecreased() {
        int totalAntes = match.getCreatures().stream().mapToInt(c -> c.getCoins()).sum();
        match.iterate();
        int totalDepois = match.getCreatures().stream().mapToInt(c -> c.getCoins()).sum();
        assertThat(totalDepois).isLessThanOrEqualTo(totalAntes);
    }

    // Teste baseado em propriedade: X deve estar entre -1.0 e 1.0 após iteração
    @Test
    public void testPositionRemainsInBounds() {
        match.iterate();
        for (var c : match.getCreatures()) {
            assertThat(c.getX()).isBetween(-1.0f, 1.0f);
        }
    }

    // Teste com dublê: Força metade das criaturas com 1 moeda
    /*@Test
    public void testHalfReachOneCoin() {
        Match customMatch = new Match(10);
        for (int i = 0; i < 5; i++) {
            var creature = customMatch.getCreatures().get(i);
            // zera moedas atuais
            creature.addCoins(-creature.getCoins());
            // adiciona 1 moeda
            creature.addCoins(1);
        }

        assertThat(customMatch.hasHalfElementsReachedOneCoin()).isTrue();
    }*/


    // Teste de Integração / Teste de Sistema: Verificar se o painel de visualização (VisualizationPanel)
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
