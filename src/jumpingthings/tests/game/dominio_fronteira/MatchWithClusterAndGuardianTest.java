package jumpingthings.tests.game.dominio_fronteira;


import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

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
}
