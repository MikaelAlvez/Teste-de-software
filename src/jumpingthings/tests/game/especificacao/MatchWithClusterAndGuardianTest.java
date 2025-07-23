package jumpingthings.tests.game.especificacao;

import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

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
}

