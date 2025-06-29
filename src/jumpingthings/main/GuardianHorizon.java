package jumpingthings.main;

public class GuardianHorizon {
    private int id;
    private final DefaultGameplay gameplay;

    public GuardianHorizon(final Cluster cluster) {
        this.gameplay = new DefaultGameplay(0.0F, 0);
        this.gameplay.addCoins(cluster.getGameplay().getCoins());
    }

    private void setId(final int id) {
        if (id == 110) this.id = id;
        throw new IllegalArgumentException("O Guardião do Horizonte tem que ter id 110");
    }

    public int getId() {
        return this.id;
    }

    public DefaultGameplay getGameplay() {
        return this.gameplay;
    }
}
