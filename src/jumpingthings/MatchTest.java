package jumpingthings;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

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
        assertThat(match.getCreatures()).hasSize(30).hasSize(30);
    }

    //  Verifica se as posições são mudadas após uma alteração
    @Test
    void shouldUpdatePositionsAfterIteration() {
        Match match = new Match(5);
        final var before = match.getCreatures().stream().map(Creature::getX).toList();
        match.iterate();
        final var after = match.getCreatures().stream().map(Creature::getX).toList();
        assertThat(after).isNotEqualTo(before); // As posições devem mudar
    }

    // Verifica se após iteração há transferência de moedas
    @Test
    public void testIterateSomaMetadeDasMoedas() {
        Match match = new Match(2);
        List<Creature> creatures = match.getCreatures();
        int moedasAntes = creatures.get(0).getCoins();
        for (int i = 0; i < 100; i++) match.iterate();
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
        Match match = new Match(30);
        Creature c1 = match.getCreatures().get(29);
        match.setMaxDistanceStealCoins(2.0f);
        while (match.getCreatures().get(29).getCoins() != 1) match.iterate();
        assertThat(c1.getCoins()).isEqualTo(1);
    }

    // Não deve roubar moeda
    @Test
    void fd() {
        final var match = new Match(2);
        match.setMaxDistanceStealCoins(0.01f);
        match.iterate();
        final var c1 = match.getCreatures().get(0);
        assertThat(c1.getCoins()).isEqualTo(1_000_000);
    }

    // Testa se a quantidade total de moedas não muda
    @Test
    public void testEnsuresEndTotalCoinsRemainsSame() {
        final var match = new Match(10);
        for (int i = 0; i < 3; i++) match.iterate();
        final var totalCoins = match.getCreatures()
                .stream()
                .mapToInt(Creature::getCoins)
                .sum();
        assertThat(totalCoins).isEqualTo(10_000_000);
    }

    // A distãncia de comparação para roubar moeda tem que ter duas casas decimais
    @Test
    void distanceMustHaveTwoDecimalPlaces() {
        final var match = new Match(2);
        match.setMaxDistanceStealCoins(1.1555f);
        assertThat(match.getMaxDistanceStealCoins()).isEqualTo(1.16f);
    }

    // deve retornar true quando metade das criaturas tiverem uma moeda
    @Test
    void shouldReturnTrueWhenHalfCreaturesHaveACoin() {
        final var match = new Match(10);
        match.setMaxDistanceStealCoins(2.0f);
        // Roda até que a condição seja satisfeita ou limite de iterações seja atingido
        int maxIterations = 1000;
        int count = 0;
        while (count < maxIterations) {
            match.iterate();
            count++;
        }
        assertThat(match.hasHalfElementsReachedOneCoin())
                .isTrue();
    }

    // deve retornar false quando não for metade das criaturas
    @Test
    void shouldReturnFalseWhenNotHalfCreaturesHaveACoin() {
        final var match = new Match(10);
        match.setMaxDistanceStealCoins(2.0f);
        // Roda até que a condição seja satisfeita ou limite de iterações seja atingido
        int maxIterations = 10;
        int count = 0;
        while (count < maxIterations) {
            match.iterate();
            count++;
        }
        assertThat(match.hasHalfElementsReachedOneCoin())
                .isFalse();
    }

    // Testes de fronteira

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

    // não deve criar uma partida
    @Test
    public void testMatchComZeroCriaturas() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            new Match(0);
        });
        assertEquals("Número de criaturas insuficientes.", exception.getMessage());
    }

    // Todas criaturas na mesma posição
    @Test
    public void testAllSamePosition() {
        Match match = new Match(3);
        for (Creature c : match.getCreatures()) c.setX(0.0f);
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

    // Não deve alterar a máxima distância de for negativa
    @Test
    void shouldNotChangeMaximumDistanceValueNegative() {
        final var match = new Match(2);
        match.setMaxDistanceStealCoins(-1.0f);
        assertThat(match.getMaxDistanceStealCoins()).isEqualTo(0.3f);
    }

    // Não deve alterar se a distancia for zero
    @Test
    void shouldNotChangeDistanceValueZero() {
        final var match = new Match(2);
        match.setMaxDistanceStealCoins(0.0f);
        assertThat(match.getMaxDistanceStealCoins()).isEqualTo(0.3f);
    }

    // Deve alterar a partir de 0.01f
    @Test
    void shouldChangeFromGreaterThan0ToTwoDecimalPlace() {
        final var match = new Match(2);
        match.setMaxDistanceStealCoins(0.01f);
        assertThat(match.getMaxDistanceStealCoins()).isEqualTo(0.01f);
    }

    // Não deve alterar quando for maior que 2.01f
    @Test
    void shouldNotChangeForValuesGreaterThan2f() {
        final var match = new Match(2);
        match.setMaxDistanceStealCoins(2.01f);
        assertThat(match.getMaxDistanceStealCoins()).isEqualTo(0.3f);
        match.setMaxDistanceStealCoins(3.05f);
        assertThat(match.getMaxDistanceStealCoins()).isEqualTo(0.3f);
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
        match.setMaxDistanceStealCoins(2.0f);
        match.iterate();
        assertThat(c1.getCoins()).isGreaterThan(1_000_000);
    }

}
