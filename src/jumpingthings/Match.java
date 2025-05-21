package jumpingthings;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private final List<Creature> creatures;

    public Match(final int n) {
        creatures = new ArrayList<>();
        if (n <= 1) throw new RuntimeException("Número de criaturas insuficientes.");
        final var tmp = Math.min(n, 30);
        for (int i = 0; i < tmp; i++) creatures.add(new Creature(i + 1));
    }

    /**
     * Executa uma iteração da simulação.
     *
     * Este método realiza duas ações principais:
     * 1. Atualiza a posição de todas as criaturas com base em sua lógica interna.
     * 2. Para cada criatura, procura a criatura mais próxima dentro de uma distância de 0.3.
     *    Se encontrar, a criatura atual coleta metade das moedas da criatura próxima.
     *
     * Essa lógica simula interação entre criaturas, onde as mais próximas podem transferir
     * recursos (moedas), promovendo dinâmica e competição no ambiente.
     */
    public void iterate() {
        for (final var creature : creatures) creature.updatePosition();
        for (int i = 0; i < creatures.size(); i++) {
            final var current = creatures.get(i);
            final var closest = findClosestWithinDistance(i, 0.3);
            if (closest != null && current.getId() < closest.getId()) current.addCoins(closest.getHalfCoins());
        }
    }

    /**
     * Encontra a criatura mais próxima de uma criatura específica, desde que esteja
     * dentro de uma distância máxima permitida.
     *
     * @param idx índice da criatura de referência na lista.
     * @param maxDistance distância máxima permitida para considerar outra criatura como "próxima".
     * @return a criatura mais próxima dentro da distância informada, ou null se nenhuma for encontrada.
     */
    private Creature findClosestWithinDistance(int idx, double maxDistance) {
        final var current = creatures.get(idx);
        Creature closest = null;
        var shortestDist = Double.MAX_VALUE;
        for (int i = 0; i < creatures.size(); i++) {
            if (i == idx) continue;
            final var other = creatures.get(i);
            final var dist = Math.abs(current.getX() - other.getX());
            if (dist <= maxDistance && dist < shortestDist) {
                shortestDist = dist;
                closest = other;
            }
        }
        return closest;
    }

    public List<Creature> getCreatures() {
        return creatures;
    }
}