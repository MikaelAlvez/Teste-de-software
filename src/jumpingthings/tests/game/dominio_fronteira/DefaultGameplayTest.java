package jumpingthings.tests.game.dominio_fronteira;

import jumpingthings.main.game.DefaultGameplay;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultGameplayTest {

    /** DOMÍNIO: Testes de funcionalidade principal */

    // Verifica se os valores iniciais são armazenados corretamente
    @Test
    void testInitialValues() {
        final var gameplay = new DefaultGameplay(0.5f, 100);
        assertEquals(0.5f, gameplay.getX(), 0.001);
        assertEquals(100, gameplay.getCoins());
    }

    // Verifica se moedas são adicionadas corretamente
    @Test
    void testAddCoinsPositiveValue() {
        final var gameplay = new DefaultGameplay(0, 100);
        gameplay.addCoins(50);
        assertEquals(150, gameplay.getCoins());
    }

    // Verifica se getHalfCoins retorna e remove corretamente
    @Test
    void testGetHalfCoinsAndLoseCoins() {
        final var gameplay = new DefaultGameplay(0, 9);
        final var half = gameplay.getHalfCoins();
        assertEquals(4, half); // 9 / 2 = 4
        assertEquals(5, gameplay.getCoins()); // 9 - 4 = 5
    }

    /** FRONTEIRAS: Testes de limites e validação */

    /** Valor negativo não deve ser adicionado */
    @Test
    void testAddCoinsNegativeValueIgnored() {
        final var gameplay = new DefaultGameplay(0, 100);
        gameplay.addCoins(-20);
        assertEquals(100, gameplay.getCoins());
    }

    /** Não deve perder moedas se for 1 ou menos */
    @Test
    void testGetHalfCoinsWhenCoinsLessThanOrEqualToOne() {
        final var gameplay = new DefaultGameplay(0, 1);
        final var half = gameplay.getHalfCoins();
        assertEquals(0, half);
        assertEquals(1, gameplay.getCoins());
    }

    /** Teste dentro do intervalo (-1, 1), com arredondamento */
    @Test
    void testSetXWithinBounds() {
        final var gameplay = new DefaultGameplay(0, 100);
        gameplay.setX(0.789f);
        assertEquals(0.79f, gameplay.getX());
    }

    /** Valores fora de [-1, 1] devem ser ignorados */
     @Test
    void testSetXOutOfBoundsDoesNotChangeX() {
        final var gameplay = new DefaultGameplay(0.5f, 100);
        gameplay.setX(1.01f); // > 1.0 → ignorado
        assertEquals(0.5f, gameplay.getX());

        gameplay.setX(-1.05f); // < -1.0 → ignorado
        assertEquals(0.5f, gameplay.getX());
    }

    /** Limites extremos aceitos */
     @Test
    void testSetXBorderValuesAccepted() {
        final var gameplay = new DefaultGameplay(0, 100);
        gameplay.setX(1.0f);
        assertEquals(1.0f, gameplay.getX());

        gameplay.setX(-1.0f);
        assertEquals(-1.0f, gameplay.getX());
    }

    /** Verifica que moedas negativas não são atribuídas */
    @Test
    void testSetCoinsNegativeValueIgnored() {
        final var gameplay = new DefaultGameplay(0f, -500);
        assertTrue(gameplay.getCoins() >= 0, "Coins should not be negative");
    }
}

