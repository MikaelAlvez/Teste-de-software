package jumpingthings.tests.game.duble;

import jumpingthings.main.game.DefaultGameplay;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultGameplayTest {

    /** DUBLÊ DE TESTE (não determinístico): Math.random() */

    @Test
    void testUpdatePositionStaysInBounds() {
        // updatePosition usa Math.random() (r ∈ [-1, 1])
        // Testa repetidamente se X permanece no intervalo [-1, 1]
        final var gameplay = new DefaultGameplay(0f, 1_000_000);

        for (int i = 0; i < 1_000; i++) {
            gameplay.updatePosition();
            float x = gameplay.getX();
            assertTrue(x >= -1f && x <= 1f,
                    "X must remain within bounds [-1, 1] after updatePosition()");
        }
    }
}

