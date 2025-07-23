package jumpingthings.main.game;

import java.util.*;

public class MatchWithClusterAndGuardian {
    private final List<Creature> creatures;
    private final List<Cluster> clusters;
    private final GuardianHorizon guardian;
    private final float maxDistanceStealCoins = 0.3f;
    private int nextClusterId = 41;

    public MatchWithClusterAndGuardian(final int n) {
        if (n <= 1) throw new RuntimeException("Número de criaturas insuficientes.");
        this.creatures = new ArrayList<>();
        for (int i = 0; i < Math.min(n, 30); i++) creatures.add(new Creature(i + 1));
        this.clusters = new ArrayList<>();
        this.guardian = new GuardianHorizon(this.creatures.size());
    }

    public void iterate() {
        // 1. Gameplay padrão das CRIATURAS
        creaturesGamepley();

        // 2. Verificar colisões entre criaturas
        resolveCreatureCollisions();

        // 3. Clusters roubam da criatura mais próxima
        if (this.clusters.size() > 1) {
            for (final var cluster : this.clusters) cluster.getGameplay().updatePosition();
        }
        for (Cluster cluster : clusters) {
            clusterSteal(cluster, creatures);
        }

        // 4. Atualiza posição do guardião
        guardian.getGameplay().updatePosition();

        // 5. Verifica se guardião colidiu com algum cluster
        resolveGuardianCollision();
    }

    private void creaturesGamepley() {
        for (final var creature : creatures) creature.updatePosition();
        for (int i = 0; i < creatures.size(); i++) {
            final var current = creatures.get(i);
            final var closest = findClosestWithinDistance(i, maxDistanceStealCoins);
            if (closest != null && current.getId() < closest.getId()) current.addCoins(closest.getHalfCoins());
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

    private float getMaxDistanceStealCoins() {
        return this.maxDistanceStealCoins;
    }

    private void resolveCreatureCollisions() {
        final var positionMap = new HashMap<Float, List<Creature>>();
        for (final var c : creatures) positionMap
                .computeIfAbsent(c.getX(), k -> new ArrayList<>())
                .add(c);


        for (List<Creature> sameSpot : positionMap.values()) {
            if (sameSpot.size() > 1) {
                final var newCluster = new Cluster(nextClusterId++, sameSpot);
                clusters.add(newCluster);
                creatures.removeAll(sameSpot);
            }
        }
    }

    private void clusterSteal(Cluster cluster, List<Creature> others) {
        if (others.isEmpty()) return;

        float clusterX = cluster.getGameplay().getX();
        Creature closest = null;
        float minDist = Float.MAX_VALUE;

        for (Creature c : others) {
            float dist = Math.abs(c.getX() - clusterX);
            if (dist < minDist) {
                minDist = dist;
                closest = c;
            }
        }

        if (closest != null) {
            int stolen = closest.getHalfCoins();
            cluster.getGameplay().addCoins(stolen);
        }
    }

    private void resolveGuardianCollision() {
        float gx = guardian.getGameplay().getX();

        Iterator<Cluster> iterator = clusters.iterator();
        while (iterator.hasNext()) {
            Cluster cluster = iterator.next();
            if (cluster.getGameplay().getX() == gx) {
                guardian.getGameplay().addCoins(cluster.getGameplay().getCoins());
                iterator.remove();
            }
        }
    }

    public boolean isFinished() {
        int totalEntities = creatures.size() + clusters.size();
        boolean onlyGuardianLeft = totalEntities == 0;
        boolean oneCreatureVsGuardian = totalEntities == 1 && creatures.size() == 1 &&
                guardian.getGameplay().getCoins() > creatures.get(0).getCoins();
        return onlyGuardianLeft || oneCreatureVsGuardian;
    }

    public void reset() {
        this.creatures.clear();
        this.clusters.clear();
        this.nextClusterId = 41;

        // Recria as criaturas
        for (int i = 0; i < 30; i++) {
            this.creatures.add(new Creature(i + 1));
        }

        // Reinicia o guardião com novas moedas e posição
        this.guardian.getGameplay().reset();
    }

    public boolean hasHalfElementsReachedOneCoin() {
        int total = creatures.size() + clusters.size();
        if (total == 0) return false;

        long count = creatures.stream()
                .filter(c -> c.getCoins() == 1)
                .count();

        count += clusters.stream()
                .filter(cl -> cl.getGameplay().getCoins() == 1)
                .count();

        return count >= Math.ceil(total / 2.0);
    }

    public GuardianHorizon getGuardian() {
        return guardian;
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }
}
