package jumpingthings.main.game;

import java.util.List;

public class Cluster {
    private int id;
    private final DefaultGameplay gameplay;

    public Cluster(final int id, final List<Creature> creatures) {
        setId(id);
        this.gameplay = new DefaultGameplay(0.0F, 0);
        this.gameplay.addCoins(creatures.stream().mapToInt(Creature::getCoins).sum());
        this.gameplay.setX(creatures.getFirst().getX());
    }

    private void setId(final int id) {
        if (id <= 40 || id >= 100) throw new IllegalArgumentException("Faixa de id inválida!");
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public DefaultGameplay getGameplay() {
        return this.gameplay;
    }
}
