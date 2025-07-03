package jumpingthings.main.game;

public class GuardianHorizon {
    private int id;
    private final DefaultGameplay gameplay;

    public GuardianHorizon(final int id) {
        this.gameplay = new DefaultGameplay(0.0F, 0);
        setId(id);
    }

    public void addCluster(final Cluster cluster) {
        if (cluster == null) throw new IllegalArgumentException("Cluster não pode ser nulo!");
        this.gameplay.addCoins(cluster.getGameplay().getCoins());
    }

    private void setId(final int id) {
        if (id <= 1) throw new IllegalArgumentException("Guardião precisa de um id maior que 1");
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public DefaultGameplay getGameplay() {
        return this.gameplay;
    }
}
