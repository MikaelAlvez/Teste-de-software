package Avaliação1;

public class Criatura {
    private final int id;
    private double xi;
    private double gi;

    public Criatura(int id) {
        this.id = id;
        this.gi = 1_000_000;
        this.xi = 0.0;
    }

    public void atualizarPosicao() {
        double r = -1 + 2 * Math.random(); // r ∈ [-1, 1]
        this.xi += r * this.gi;
    }

    public int getId() { return id; }
    public double getXi() { return xi; }
    public double getGi() { return gi; }

    public void addOuro(double ouro) { this.gi += ouro; }
    public void removeOuro(double ouro) { this.gi -= ouro; }
}
