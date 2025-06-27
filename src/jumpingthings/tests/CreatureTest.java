package jumpingthings.tests;

import jumpingthings.main.Creature;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;;

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

    // Não deve criar uma criatura com os 3 parametros inválidos
    @Test
    void shouldNotCreateCreatureWithInvalidXAndNegativeCoins() {
        final var c = new Creature(4, 2.0f, -100); // x inválido, moedas negativas
        assertThat(c.getX()).isEqualTo(0.0f); // x resetado
        assertThat(c.getCoins()).isEqualTo(1_000_000); // moedas default
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

    // deve poder criar uma criatura informando o id e a posicão inicial
    @Test
    void shouldPossibleCreateCreatureWithIDAndPositionX() {
        final var creature = new Creature(2, 0.1f);
        assertThat(creature.getX()).isEqualTo(0.1f);
        final var creature2 = new Creature(3, -0.5668f);
        assertThat(creature2.getX()).isNotEqualTo(-0.56668f);
        assertThat(creature2.getX()).isEqualTo(-0.57f);
    }

    @Test
    void shouldNotBePossibleCreateCreatureWithInvalidPositionX() {
        final var creature = new Creature(2, -2.0f);
        assertThat(creature.getX()).isEqualTo(0.0f);
    }

    // não deve criar criatura com ids negativos
    @Test
    void shouldNotBePossibleCreateCreaturesWithNegativesIds() {
        final var exception = assertThrows(RuntimeException.class, () -> {
            new Creature(-1); // valor inválido (n <= 1)
        });
        assertEquals("Id inválido.", exception.getMessage());
    }

    // Testes de fronteira

    // Usando Integer.MAX_VALUE como moedas
    @Test
    public void testMaxIntCoins() {
        Creature c = new Creature(1, Integer.MAX_VALUE);
        assertThat(c.getCoins()).isEqualTo(Integer.MAX_VALUE);
    }

    // deve ser possível criar criaturas com Ids positivos mais o zero
    @Test
    void shouldBePossibleCreateCreaturesWithPositiveIdsPlusZero() {
        final var creature = new Creature(0);
        final var creature2 = new Creature(1);
        assertThat(creature.getId()).isEqualTo(0);
        assertThat(creature2.getId()).isEqualTo(1);
    }

    // Não deve alterar o valor das moedas para valores menores que zero
    @Test
    void shouldNotAbleChangeValueCoinsWithNegativesValues() {
        final var creature = new Creature(3, -1);
        assertThat(creature.getCoins()).isEqualTo(1_000_000);
        final var creature2 = new Creature(2, 0);
        assertThat(creature2.getCoins()).isEqualTo(0);
    }

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
