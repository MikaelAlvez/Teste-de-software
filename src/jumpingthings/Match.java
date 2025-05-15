package com.lucassf2k.main.jumpingthings;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private final List<Creature> creatures;

    public Match(int n) {
        creatures = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            creatures.add(new Creature(i + 1));
        }
    }

    public void iterate() {
        // Atualiza a posição de todas as criaturas
        for (final var creature : creatures) {
            creature.updatePosition();
        }

        // Itera em ordem de ID (índice da lista)
        for (int i = 0; i < creatures.size(); i++) {
            final var current = creatures.get(i);
            final var closest = findClosestWithinDistance(i, 0.3);

            // Se achou uma criatura próxima o suficiente
            if (closest != null) {
                current.addCoins(closest.getHalfCoins());
            }
        }
    }

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

//    public void iterate() {
//        for (final var creature : creatures) {
//            creature.updatePosition();
//        }
//
//        for (int i = 0; i < creatures.size(); i++) {
//            final var current = creatures.get(i);
//            final var closest = findClosest(i);
//            if (closest != null) {
//                current.addCoins(closest.getHalfCoins());
//            }
//        }
//    }
//
//    private Creature findClosest(int idx) {
//        final var current = creatures.get(idx);
//        Creature closest = null;
//        var shortestDist = Double.MAX_VALUE;
//        for (int i = 0; i < creatures.size(); i++) {
//            if (i == idx) continue;
//            final var other = creatures.get(i);
//            final var dist = Math.abs(current.getX() - other.getX());
//            if (dist < shortestDist) {
//                shortestDist = dist;
//                closest = other;
//            }
//        }
//        return closest;
//    }

    public List<Creature> getCreatures() {
        return creatures;
    }
}