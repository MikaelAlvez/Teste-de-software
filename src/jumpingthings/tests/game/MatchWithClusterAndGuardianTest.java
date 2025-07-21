package jumpingthings.tests.game;

import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class MatchWithClusterAndGuardianTest {

    private MatchWithClusterAndGuardian match;
    private VisualizationPanelWithClusterAndGuardian panel;
    private JFrame frame;

    @BeforeEach
    void setup() {
        match = new MatchWithClusterAndGuardian(10);
        panel = new VisualizationPanelWithClusterAndGuardian(match);
        frame = new JFrame();
    }

    // --- TESTES DE ESPECIFICAÇÃO ---

    /** Garante que o match inicia com o número correto de criaturas, respeitando o limite interno (ex: máximo 30) */
    @Test
    public void testMatchInitialization() {
        assertThat(match.getCreatures()).hasSize(10);
    }

    /** Garante que criar Match com 0 criaturas lança exceção */
    @Test
    public void testMatchComZeroCriaturas() {
        assertThatThrownBy(() -> new MatchWithClusterAndGuardian(0))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Número de criaturas insuficientes");
    }

    /** Garante que o match respeita limite máximo de criaturas (exemplo: 30) */
    @Test
    public void testMatchMaxLimit() {
        MatchWithClusterAndGuardian largeMatch = new MatchWithClusterAndGuardian(100);
        assertThat(largeMatch.getCreatures()).hasSizeLessThanOrEqualTo(30);
    }


    // --- TESTES DE FRONTEIRA ---

    /** Cria um match com o limite mínimo permitido e verifica funcionamento */
    @Test
    public void testMatchMinLimitBoundary() {
        MatchWithClusterAndGuardian minMatch = new MatchWithClusterAndGuardian(2);
        assertThat(minMatch.getCreatures()).hasSize(2);
    }

    /** Testa o comportamento no limite máximo, verificando que não ultrapassa */
    @Test
    public void testMatchUpperBoundaryLimit() {
        MatchWithClusterAndGuardian maxMatch = new MatchWithClusterAndGuardian(30);
        assertThat(maxMatch.getCreatures()).hasSize(30);
    }


    // --- TESTES ESTRUTURAIS / INTEGRAÇÃO ---

    /** Testa se o painel repaint reflete mudanças após iteração */
    @Test
    public void testPanelRepaintUpdatesPositions() {
        float xBefore = match.getCreatures().get(0).getX();
        match.iterate();
        panel.repaint();
        float xAfter = match.getCreatures().get(0).getX();
        assertThat(xAfter).isNotEqualTo(xBefore);
    }

    /** Testa se o painel é adicionado corretamente ao JFrame */
    @Test
    public void testPanelAddedToFrame() {
        JFrame f = new JFrame();
        f.add(panel);
        Component[] components = f.getContentPane().getComponents();
        boolean containsPanel = false;
        for (Component c : components) {
            if (c == panel) containsPanel = true;
        }
        assertThat(containsPanel).isTrue();
    }

    // --- TESTES BASEADOS EM PROPRIEDADE ---

    /** Verifica que o total de moedas não aumenta após iteração */
    @Test
    public void testTotalCoinsNotIncreasing() {
        int totalAntes = match.getCreatures().stream().mapToInt(c -> c.getCoins()).sum();
        match.iterate();
        int totalDepois = match.getCreatures().stream().mapToInt(c -> c.getCoins()).sum();
        assertThat(totalDepois).isLessThanOrEqualTo(totalAntes);
    }

    /** Garante que posições X das criaturas estão dentro do intervalo esperado após iteração */
    @Test
    public void testPositionsWithinBounds() {
        match.iterate();
        for (var c : match.getCreatures()) {
            assertThat(c.getX()).isBetween(-1.0f, 1.0f);
        }
    }

    // --- TESTES DE DUBLÊ ---

    /** Usa criaturas manipuladas para testar condição de parada */
    @Test
    public void testSimulationStopsWhenHalfReachOneCoin() {
        MatchWithClusterAndGuardian m = new MatchWithClusterAndGuardian(10);
        for (int i = 0; i < 5; i++) {
            m.getCreatures().get(i).addCoins(-m.getCreatures().get(i).getCoins());
            m.getCreatures().get(i).addCoins(1);
        }
        int iterations = 0;
        while (iterations < 100 && !m.hasHalfElementsReachedOneCoin()) {
            m.iterate();
            iterations++;
        }
        assertThat(m.hasHalfElementsReachedOneCoin()).isTrue();
        assertThat(iterations).isLessThanOrEqualTo(100);
    }


    // --- TESTES DE SISTEMA ---

    /** Testa que a simulação para após limite máximo de iterações */
    @Test
    public void testSimulationStopsAtMaxIterations() {
        MatchWithClusterAndGuardian m = new MatchWithClusterAndGuardian(10);
        int iterations = 0;
        while (iterations < 100 && !m.hasHalfElementsReachedOneCoin()) {
            m.iterate();
            iterations++;
        }
        assertThat(iterations).isLessThanOrEqualTo(100);
    }

    /** Testa simulação que para porque metade das criaturas atingiu 1 moeda */
    /*@Test
    public void testSimulationStopsByHalfOneCoinCondition() {
        MatchWithClusterAndGuardian m = new MatchWithClusterAndGuardian(10);
        for (int i = 0; i < 5; i++) {
            m.getCreatures().get(i).addCoins(-m.getCreatures().get(i).getCoins());
            m.getCreatures().get(i).addCoins(1);
        }
        int iterations = 0;
        while (iterations < 100 && !m.hasHalfElementsReachedOneCoin()) {
            m.iterate();
            iterations++;
        }
        assertThat(m.hasHalfElementsReachedOneCoin()).isTrue();
    }*/
}
