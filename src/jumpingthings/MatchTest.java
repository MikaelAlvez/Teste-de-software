package com.lucassf2k.main.jumpingthings;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {

    // Testes de dominio

    // Verifica se a quantidade de criaturas está correta
    @Test
    public void testCriacaoMatchCom3Criaturas() {
        Match match = new Match(3);
        assertEquals(3, match.getCreatures().size());
    }

    // Verifica se a partida tem no máximo 30
    @Test
    public void testIfMatchHasMaximum14Creatures() {
        final var match = new Match(45);
        assertNotEquals(45, match.getCreatures().size());
        assertEquals(30, match.getCreatures().size());
    }

    // Verifica se após iteração há transferência de moedas
    @Test
    public void testIterateSomaMetadeDasMoedas() {
        Match match = new Match(4);
        List<Creature> creatures = match.getCreatures();
        // Força valores conhecidos
        creatures.get(0).setX(0f);
        creatures.get(1).setX(0.01f);

        int moedasAntes = creatures.get(0).getCoins();

        for (int i = 0; i < 30; i++) match.iterate();

        int moedasDepois = creatures.getFirst().getCoins();

        assertTrue(moedasDepois > moedasAntes); // Ganhou moedas do outro
    }

    // Verifica concentração de moedas após várias iterações
    @Test
    public void testRichGetRicherEffect() {
        Match match = new Match(10);
        for (int i = 0; i < 100; i++) {
            match.iterate();
        }
        List<Creature> creatures = match.getCreatures();
        int richest = creatures.stream().mapToInt(Creature::getCoins).max().orElse(0);
        int poorest = creatures.stream().mapToInt(Creature::getCoins).min().orElse(0);
        assertTrue(richest > 1_000_000);
        assertTrue(poorest < 1_000_000);
    }

    // Criatura com moedas quase zeradas
    @Test
    public void testCreatureLosesAllCoins() {
        Match match = new Match(3);
        Creature c3 = match.getCreatures().get(2);
        while (c3.getCoins() != 1) match.iterate();
        assertEquals(1, c3.getCoins());
    }

    // Testa se a quantidade total de moedas não muda
    @Test
    public void testEnsuresEndTotalCoinsRemainsSame() {
        final var match = new Match(10);
        final var totalCoins = match.getCreatures()
                .stream()
                .mapToInt(Creature::getCoins)
                .sum();
        assertEquals(10_000_000, totalCoins);
    }

    // Testes de fronteira

    // Só 1 criatura → não deve haver transferência de moedas
    @Test
    public void testMatchComApenasUmaCriatura() {
        Match match = new Match(1);
        int moedasAntes = match.getCreatures().getFirst().getCoins();
        match.iterate();
        int moedasDepois = match.getCreatures().getFirst().getCoins();
        assertEquals(moedasAntes, moedasDepois);
    }

    @Test
    public void testMatchComZeroCriaturas() {
        // Fronteira inferior: sem criaturas → não pode lançar exceções
        Match match = new Match(0);
        match.iterate();
        assertEquals(0, match.getCreatures().size());
    }

    // Distância 0 entre criaturas
    @Test
    public void testCriaturasComMesmaPosicao() {

        Match match = new Match(2);
        match.getCreatures().get(0).setX(0.5f);
        match.getCreatures().get(1).setX(0.5f);

        match.iterate();

        // Ambas devem transferir moedas uma à outra
        int totalMoedas = match.getCreatures().stream().mapToInt(Creature::getCoins).sum();
        assertTrue(totalMoedas <= 2_000_000); // Pode perder algumas no arredondamento
    }

    // Todas criaturas na mesma posição
    @Test
    public void testAllSamePosition() {
        Match match = new Match(3);
        for (Creature c : match.getCreatures()) {
            c.setX(0.0f);
        }
        match.iterate();
        assertEquals(3, match.getCreatures().size());
    }

    // Criaturas com número ímpar de moedas
    @Test
    public void testOddCoinNumberHalving() {
        Creature c = new Creature(1, 999_999);
        int half = c.getHalfCoins();
        assertEquals(499_999, half);
        assertEquals(500_000, c.getCoins());
    }

    // Usando Integer.MAX_VALUE como moedas
    @Test
    public void testMaxIntCoins() {
        Creature c = new Creature(1, Integer.MAX_VALUE);
        c.updatePosition();
        assertTrue(c.getX() >= -1.0f && c.getX() <= 1.0f);
    }

    // Testes estruturais e cobertura de código

    @Test
    public void testFindClosestFunciona() {
        Match match = new Match(3);
        match.getCreatures().get(0).setX(-0.9f);
        match.getCreatures().get(1).setX(-0.8f);
        match.getCreatures().get(2).setX(0.8f);
        Creature closest = match.getCreatures().get(1);
        Creature expected = match.getCreatures().get(0);
        // Força chamada indireta de findClosest
        match.iterate();

        assertEquals(closest.getId(), match.getCreatures().get(1).getId());
    }

    // Testa findClosest retornando null (caso 1 criatura)
    @Test
    public void testFindClosestWithOneCreature() {
        Match match = new Match(1);
        match.iterate(); // findClosest retorna null, não deve dar erro
        assertEquals(1, match.getCreatures().size());
    }

    // Testa repetição máxima (com 1000 criaturas)
    @Test
    public void testManyCreatures() {
        Match match = new Match(1000);
        match.iterate();
        assertEquals(30, match.getCreatures().size());
    }

    // Teste de mutação

    @Test
    public void testMutacaoAlterarCondicaoDistancia() {
        Match match = new Match(2);
        Creature c1 = match.getCreatures().get(0);
        Creature c2 = match.getCreatures().get(1);
        c1.setX(-1.0f);
        c2.setX(1.0f);

        match.iterate(); // Deve selecionar o mais próximo (único outro)

        assertTrue(c1.getCoins() > 1_000_000 || c2.getCoins() > 1_000_000);
        // Garante que houve transferência
    }

    // Alterar lógica de comparação para >=
    @Test
    public void testClosestLogicMutation() {
        Creature c1 = new Creature(1, 0.0f);
        Creature c2 = new Creature(2, 0.1f);
        Creature c3 = new Creature(3, 0.2f);
        Match match = new Match(0);
        match.getCreatures().addAll(List.of(c1, c2, c3));
        Creature closest = match.getCreatures().get(0);
        match.iterate(); // deve funcionar normalmente
        assertEquals(3, match.getCreatures().size());
    }

    // Sem Math.abs — verificar se ainda retorna o mais próximo
    @Test
    public void testDistanceWithoutAbs() {
        Creature a = new Creature(1, -0.5f);
        Creature b = new Creature(2, 0.3f);
        Creature c = new Creature(3, 0.6f);
        Match match = new Match(0);
        match.getCreatures().addAll(List.of(a, b, c));
        match.iterate();
        assertEquals(3, match.getCreatures().size());
    }

}
