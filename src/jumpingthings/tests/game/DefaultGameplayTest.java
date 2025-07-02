package jumpingthings.tests.game;


import jumpingthings.main.game.DefaultGameplay;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultGameplayTest {
    // ===============================
    // 🌐 DOMÍNIO: Testes de funcionalidade principal
    // ===============================

    @Test
    void testInitialValues() {
        // Verifica se os valores iniciais são armazenados corretamente
        final var gameplay = new DefaultGameplay(0.5f, 100);
        assertEquals(0.5f, gameplay.getX(), 0.001);
        assertEquals(100, gameplay.getCoins());
    }

    @Test
    void testAddCoinsPositiveValue() {
        // Verifica se moedas são adicionadas corretamente
        final var gameplay = new DefaultGameplay(0, 100);
        gameplay.addCoins(50);
        assertEquals(150, gameplay.getCoins());
    }

    @Test
    void testGetHalfCoinsAndLoseCoins() {
        // Verifica se getHalfCoins retorna e remove corretamente
        final var gameplay = new DefaultGameplay(0, 9);
        final var half = gameplay.getHalfCoins();
        assertEquals(4, half); // 9 / 2 = 4
        assertEquals(5, gameplay.getCoins()); // 9 - 4 = 5
    }

    @Test
    void testToTwoDecimalPlacesRounding() {
        // Verifica arredondamento com HALF_EVEN (0.234 → 0.23, 0.235 → 0.24)
        final var gameplay = new DefaultGameplay(0, 100);
        gameplay.setX(0.234f);
        assertEquals(0.23f, gameplay.getX(), 0.001f);

        gameplay.setX(0.235f);
        assertEquals(0.24f, gameplay.getX(), 0.001f);
    }

    // ===============================
    // 🔲 FRONTEIRAS: Testes de limites e validação
    // ===============================

    @Test
    void testAddCoinsNegativeValueIgnored() {
        // Valor negativo não deve ser adicionado
        final var gameplay = new DefaultGameplay(0, 100);
        gameplay.addCoins(-20);
        assertEquals(100, gameplay.getCoins());
    }

    @Test
    void testGetHalfCoinsWhenCoinsLessThanOrEqualToOne() {
        // Não deve perder moedas se for 1 ou menos
        final var gameplay = new DefaultGameplay(0, 1);
        final var half = gameplay.getHalfCoins();
        assertEquals(0, half);
        assertEquals(1, gameplay.getCoins());
    }

    @Test
    void testSetXWithinBounds() {
        // Teste dentro do intervalo (-1, 1), com arredondamento
        final var gameplay = new DefaultGameplay(0, 100);
        gameplay.setX(0.789f);
        assertEquals(0.79f, gameplay.getX());
    }

    @Test
    void testSetXOutOfBoundsDoesNotChangeX() {
        // Valores fora de [-1, 1] devem ser ignorados
        final var gameplay = new DefaultGameplay(0.5f, 100);
        gameplay.setX(1.01f); // > 1.0 → ignorado
        assertEquals(0.5f, gameplay.getX());

        gameplay.setX(-1.05f); // < -1.0 → ignorado
        assertEquals(0.5f, gameplay.getX());
    }

    @Test
    void testSetXBorderValuesAccepted() {
        // Limites extremos aceitos
        final var gameplay = new DefaultGameplay(0, 100);
        gameplay.setX(1.0f);
        assertEquals(1.0f, gameplay.getX());

        gameplay.setX(-1.0f);
        assertEquals(-1.0f, gameplay.getX());
    }

    @Test
    void testSetCoinsNegativeValueIgnored() {
        // Verifica que moedas negativas não são atribuídas
        final var gameplay = new DefaultGameplay(0f, -500);
        assertTrue(gameplay.getCoins() >= 0, "Coins should not be negative");
    }

    @Test
    void testCoinsCannotBeSetToNegative() {
        // Valor negativo no construtor não altera coins
        final var gameplay = new DefaultGameplay(0, -1000);
        assertEquals(1_000_000, gameplay.getCoins(), "Coins must remain default if negative passed");
    }

    // ===============================
    // 🎲 DUBLÊ DE TESTE (não determinístico): Math.random()
    // ===============================

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
