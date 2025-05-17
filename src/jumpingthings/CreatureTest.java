package com.lucassf2k.main.jumpingthings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreatureTest {

    // Testes de domínio

    @Test
    void testCreateCreatureWithDefaultValues() {
        Creature c = new Creature(1);
        assertEquals(1, c.getId());
        assertEquals(1_000_000, c.getCoins());
        assertEquals(0.0f, c.getX());
    }

    // Valores customizados
    @Test
    void testCreateCreatureWithCustomValues() {
        Creature c = new Creature(2, 0.75f, 500_000);
        assertEquals(2, c.getId());
        assertEquals(0.75f, c.getX());
        assertEquals(500_000, c.getCoins());
    }

    // Adicionar moedas
    @Test
    void testAddCoins() {
        Creature c = new Creature(7, 500_000);
        c.addCoins(100_000);
        assertEquals(600_000, c.getCoins());
    }

    // Não adiciona moedas negativas
    @Test
    void testAddNegativeCoinsDoesNothing() {
        Creature c = new Creature(8, 500_000);
        c.addCoins(-200_000);
        assertEquals(500_000, c.getCoins());
    }

    // GetHalfCoins reduz pela metade
    @Test
    void testGetHalfCoinsReducesByHalf() {
        Creature c = new Creature(10, 1_000_000);
        int half = c.getHalfCoins();
        assertEquals(500_000, half);
        assertEquals(500_000, c.getCoins());
    }

    // Testes de fronteira

    // X = -1
    @Test
    void testSetXToMinimumBoundary() {
        Creature c = new Creature(3);
        c.setX(-1f);
        assertEquals(-1f, c.getX());
    }

    // X = 1
    @Test
    void testSetXToMaximumBoundary() {
        Creature c = new Creature(4);
        c.setX(1f);
        assertEquals(1f, c.getX());
    }

    // X < -1 (não deve alterar)
    @Test
    void testSetXBelowMinimumBoundary() {
        Creature c = new Creature(5);
        c.setX(-1.01f);
        assertEquals(0.0f, c.getX()); // valor original
    }

    // X > 1 (não deve alterar)
    @Test
    void testSetXAboveMaximumBoundary() {
        Creature c = new Creature(6);
        c.setX(1.01f);
        assertEquals(0.0f, c.getX()); // valor original
    }

    // Teste estrutural + domínio

    // UpdatePosition altera X e respeita limites
    @Test
    void testUpdatePositionWithinBounds() {
        Creature c = new Creature(9, 0f, 1_000_000);
        c.updatePosition();
        float x = c.getX();
        assertTrue(x >= -1f && x <= 1f);
    }

    // GetHalfCoins com 1 moeda (não deve perder moeda)
    @Test
    void testGetHalfCoinsWhenCoinsIsOne() {
        Creature c = new Creature(11, 1);
        int half = c.getHalfCoins();
        assertEquals(0, half); // metade de 1 é 0 (inteiro)
        assertEquals(1, c.getCoins()); // não deve perder moeda
    }

    // Teste estrutural

    // SetCoins com valor negativo (não deve alterar)
    @Test
    void testSetNegativeCoinsDoesNothing() {
        Creature c = new Creature(12, 100);
        c.addCoins(-1000);
        assertEquals(100, c.getCoins());
    }

    // X é arredondado para duas casas
    @Test
    void testXIsRoundedToTwoDecimalPlaces() {
        Creature c = new Creature(13);
        c.setX(0.67891f);
        assertEquals(0.68f, c.getX());
    }
}
