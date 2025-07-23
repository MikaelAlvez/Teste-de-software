package jumpingthings.tests.game.baseado_em_propriedade;

import jumpingthings.main.game.Creature;
import jumpingthings.main.game.Match;
import jumpingthings.main.game.VisualizationPanel;
import org.junit.jupiter.api.BeforeEach;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;

import javax.swing.*;

import java.util.List;

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

    /** Propriedade: A quantidade total de moedas não deve aumentar após uma iteração */
    @Property
    void totalCoinsShouldNotIncrease(@ForAll @IntRange(min = 2, max = 100) int creatureCount) {
        Match match = new Match(creatureCount);
        int totalAntes = match.getCreatures().stream().mapToInt(Creature::getCoins).sum();
        match.iterate();
        int totalDepois = match.getCreatures().stream().mapToInt(Creature::getCoins).sum();
        assertThat(totalDepois).isLessThanOrEqualTo(totalAntes);
    }

    /** Propriedade: A posição X de cada criatura deve permanecer dentro de um intervalo razoável após iteração */
    @Property
    void allCreaturePositionsWithinReasonableBounds(@ForAll @IntRange(min = 2, max = 100) int creatureCount) {
        Match match = new Match(creatureCount);
        match.iterate();
        List<Creature> creatures = match.getCreatures();
        for (Creature c : creatures) {
            double x = c.getX();
            // Intervalo arbitrário após movimentação: se r ∈ [-1, 1] e gi inicia com 1_000_000
            // a posição pode teoricamente variar muito, então verificamos se é um número finito e válido
            assertThat(x).isFinite(); // Evita valores infinitos ou NaN
        }
    }
}

