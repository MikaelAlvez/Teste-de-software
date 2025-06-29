package jumpingthings.main;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private int id;
    private final List<Creature> members = new ArrayList<>();
    private final DefaultGameplay gameplay;

    public Cluster(final int id, final List<Creature> creatures) {
        setId(id);
        this.gameplay = new DefaultGameplay(0.0F, 0);
        setMembers(creatures);
    }

    private void setId(final int id) {
        if (id <= 40 || id >= 100) throw new IllegalArgumentException("Faixa de id inválida!");
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setMembers(final List<Creature> creatures) {
        if (!creatures.isEmpty()) {
            this.members.addAll(creatures);
            final var totalCoins = this.members.stream().mapToInt(Creature::getCoins).sum();
            this.gameplay.addCoins(totalCoins);
        };
    }

    public List<Creature> getMembers() {
        return this.members;
    }

    public DefaultGameplay getGameplay() {
        return this.gameplay;
    }
}
