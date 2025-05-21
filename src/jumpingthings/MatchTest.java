package jumpingthings;

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
        assertThat(match.getCreatures()).hasSize(3);
    }

    // Verifica se a partida tem no máximo 30
    @Test
    public void testIfMatchHasMaximum30Creatures() {
        final Match match = new Match(45);

        assertThat(match.getCreatures()).hasSize(30).doesNotHaveSize(45);
    }

    // Deve lançar uma Exception quando colocar um números que 1
    @Test
    public void testIfThrowsExceptionWhenInvalidCreatureValueEnteredMatch() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            new Match(1); // valor inválido (n <= 1)
        });
        assertEquals("Número de criaturas insuficientes.", exception.getMessage());
        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> {
            new Match(-5); // valor inválido (n <= 1)
        });
        assertEquals("Número de criaturas insuficientes.", exception2.getMessage());
    }


    // Verifica se após iteração há transferência de moedas
    @Test
    public void testIterateSomaMetadeDasMoedas() {
        Match match = new Match(2);
        List<Creature> creatures = match.getCreatures();

        creatures.get(0).setX(0f);
        creatures.get(1).setX(0.01f);

        int moedasAntes = creatures.get(0).getCoins();
        match.iterate();
        int moedasDepois = creatures.get(0).getCoins();

        assertThat(moedasDepois).isGreaterThan(moedasAntes);
    }


    // Verifica concentração de moedas após várias iterações
    @Test
    public void testRichGetRicherEffect() {
        Match match = new Match(10);
        for (int i = 0; i < 100; i++) match.iterate();

        List<Creature> creatures = match.getCreatures();
        int richest = creatures.stream().mapToInt(Creature::getCoins).max().orElse(0);
        int poorest = creatures.stream().mapToInt(Creature::getCoins).min().orElse(0);

        assertThat(richest).isGreaterThan(1_000_000);
        assertThat(poorest).isLessThan(1_000_000);
    }

    // Criatura com moedas quase zeradas
    @Test
    public void testCreatureLosesAllCoins() {
        Match match = new Match(2);
        Creature c1 = match.getCreatures().get(0);
        Creature c2 = match.getCreatures().get(1);

        for (int i = 0; i < 100; i++) match.iterate();

        assertThat(c1.getCoins() <= 1 || c2.getCoins() <= 1).isTrue();
    }

    // Testa se a quantidade total de moedas não muda
    @Test
    public void testEnsuresEndTotalCoinsRemainsSame() {
        final var match = new Match(10);
        final var totalCoins = match.getCreatures()
                .stream()
                .mapToInt(Creature::getCoins)
                .sum();

        assertThat(totalCoins).isEqualTo(10_000_000);
    }

    // Testes de fronteira

    // Só 1 criatura → não deve haver transferência de moedas
    @Test
    public void testMatchComApenasUmaCriatura() {
        Match match = new Match(1);
        int moedasAntes = match.getCreatures().getFirst().getCoins();
        match.iterate();
        int moedasDepois = match.getCreatures().getFirst().getCoins();

        assertThat(moedasDepois).isEqualTo(moedasAntes);
    }

    @Test
    public void testMatchComZeroCriaturas() {
        Match match = new Match(0);
        match.iterate();

        assertThat(match.getCreatures()).isEmpty();
    }

    // Distância 0 entre criaturas
    @Test
    public void testCriaturasComMesmaPosicao() {
        Match match = new Match(2);
        match.getCreatures().get(0).setX(0.5f);
        match.getCreatures().get(1).setX(0.5f);
        match.iterate();

        int totalMoedas = match.getCreatures()
                .stream()
                .mapToInt(Creature::getCoins)
                .sum();

        assertThat(totalMoedas).isLessThanOrEqualTo(2_000_000);
    }

    // Todas criaturas na mesma posição
    @Test
    public void testAllSamePosition() {
        Match match = new Match(3);
        for (Creature c : match.getCreatures()) {
            c.setX(0.0f);
        }
        match.iterate();

        assertThat(match.getCreatures()).hasSize(3);
    }

    // Criaturas com número ímpar de moedas
    @Test
    public void testOddCoinNumberHalving() {
        Creature c = new Creature(1, 999_999);
        int half = c.getHalfCoins();

        assertThat(half).isEqualTo(499_999);
        assertThat(c.getCoins()).isEqualTo(500_000);
    }

    // Usando Integer.MAX_VALUE como moedas
    @Test
    public void testMaxIntCoins() {
        Creature c = new Creature(1, Integer.MAX_VALUE);
        c.updatePosition();

        assertThat(c.getX()).isBetween(-1.0f, 1.0f);
    }

    // Testes estruturais e cobertura de código

    @Test
    public void testFindClosestFunciona() {
        Match match = new Match(3);
        match.getCreatures().get(0).setX(-0.9f);
        match.getCreatures().get(1).setX(-0.8f);
        match.getCreatures().get(2).setX(0.8f);
        Creature original = match.getCreatures().get(1);

        match.iterate();

        assertThat(match.getCreatures()).anyMatch(c -> c.getId() == original.getId());
    }

    // Testa findClosest retornando null (caso 1 criatura)
    @Test
    public void testFindClosestWithOneCreature() {
        Match match = new Match(1);
        match.iterate();

        assertThat(match.getCreatures()).hasSize(1);
    }

    // Testa repetição máxima (com 1000 criaturas)
    @Test
    public void testManyCreatures() {
        Match match = new Match(1000);
        match.iterate();

        assertThat(match.getCreatures()).hasSize(30);
    }

    // Teste de mutação

    @Test
    public void testMutacaoAlterarCondicaoDistancia() {
        Match match = new Match(2);
        Creature c1 = match.getCreatures().get(0);
        Creature c2 = match.getCreatures().get(1);
        c1.setX(-1.0f);
        c2.setX(1.0f);

        match.iterate();

        assertThat(c1.getCoins() > 1_000_000 || c2.getCoins() > 1_000_000).isTrue();
    }

    // Alterar lógica de comparação para >=
    @Test
    public void testClosestLogicMutation() {
        Creature c1 = new Creature(1, 0.0f);
        Creature c2 = new Creature(2, 0.1f);
        Creature c3 = new Creature(3, 0.2f);
        Match match = new Match(0);
        match.getCreatures().addAll(List.of(c1, c2, c3));

        match.iterate();

        assertThat(match.getCreatures()).hasSize(3);
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

        assertThat(match.getCreatures()).hasSize(3);
    }

    @Test
    public void testIfMatchHasMaximum30Creatures() {
        Match match = new Match(45);

        assertThat(match.getCreatures()).hasSize(30).doesNotHaveSize(45);
    }

}
