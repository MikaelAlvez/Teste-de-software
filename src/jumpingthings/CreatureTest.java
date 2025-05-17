package jumpingthings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreatureTest {

    // Testes de domínio
  
    // Valores default
    @Test
    void testCreateCreatureWithDefaultValues() {
        Creature c = new Creature(1);
        assertThat(c.getId()).isEqualTo(1);
        assertThat(c.getCoins()).isEqualTo(1_000_000);
        assertThat(c.getX()).isEqualTo(0.0f);
    }

    // Valores customizados
    @Test
    void testCreateCreatureWithCustomValues() {
        Creature c = new Creature(2, 0.75f, 500_000);
        assertThat(c.getId()).isEqualTo(2);
        assertThat(c.getX()).isEqualTo(0.75f);
        assertThat(c.getCoins()).isEqualTo(500_000);
    }

    // Adicionar moedas
    @Test
    void testAddCoins() {
        Creature c = new Creature(7, 500_000);
        c.addCoins(100_000);
        assertThat(c.getCoins()).isEqualTo(600_000);
    }

    // Não adiciona moedas negativas
    @Test
    void testAddNegativeCoinsDoesNothing() {
        Creature c = new Creature(8, 500_000);
        c.addCoins(-200_000);
        assertThat(c.getCoins()).isEqualTo(500_000);
    }

    // GetHalfCoins reduz pela metade
    @Test
    void testGetHalfCoinsReducesByHalf() {
        Creature c = new Creature(10, 1_000_000);
        int half = c.getHalfCoins();
        assertThat(half).isEqualTo(500_000);
        assertThat(c.getCoins()).isEqualTo(500_000);
    }

    // Testes de fronteira

    // X = -1
    @Test
    void testSetXToMinimumBoundary() {
        Creature c = new Creature(3);
        c.setX(-1f);
        assertThat(c.getX()).isEqualTo(-1f);
    }

    // X = 1
    @Test
    void testSetXToMaximumBoundary() {
        Creature c = new Creature(4);
        c.setX(1f);
        assertThat(c.getX()).isEqualTo(1f);
    }

    // X < -1 (não deve alterar)
    @Test
    void testSetXBelowMinimumBoundary() {
        Creature c = new Creature(5);
        c.setX(-1.01f);
        assertThat(c.getX()).isEqualTo(0.0f);
    }

    // X > 1 (não deve alterar)
    @Test
    void testSetXAboveMaximumBoundary() {
        Creature c = new Creature(6);
        c.setX(1.01f);
        assertThat(c.getX()).isEqualTo(0.0f);
    }

    // Teste estrutural + domínio

    // UpdatePosition altera X e respeita limites
    @Test
    void testUpdatePositionWithinBounds() {
        Creature c = new Creature(9, 0f, 1_000_000);
        c.updatePosition();
        assertThat(c.getX()).isBetween(-1f, 1f);
    }

    // GetHalfCoins com 1 moeda (não deve perder moeda)
    @Test
    void testGetHalfCoinsWhenCoinsIsOne() {
        Creature c = new Creature(11, 1);
        int half = c.getHalfCoins();
        assertThat(half).isEqualTo(0);
        assertThat(c.getCoins()).isEqualTo(1);
    }

    // Teste estrutural

    // SetCoins com valor negativo (não deve alterar)
    @Test
    void testSetNegativeCoinsDoesNothing() {
        Creature c = new Creature(12, 100);
        c.addCoins(-1000);
        assertThat(c.getCoins()).isEqualTo(100);
    }

    // X é arredondado para duas casas
    @Test
    void testXIsRoundedToTwoDecimalPlaces() {
        Creature c = new Creature(13);
        c.setX(0.67891f);
        assertThat(c.getX()).isEqualTo(0.68f);
    }
}
