package jumpingthings;

import java.util.*;

public class Simulador {
    private List<Creature> criaturas;

    public Simulador(int n) {
        criaturas = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            criaturas.add(new Creature(i + 1));
        }
    }

    public void iterar() {
        for (Creature c : criaturas) {
            c.atualizarPosicao();
        }

        for (int i = 0; i < criaturas.size(); i++) {
            var atual = criaturas.get(i);
            var maisProxima = encontrarMaisProxima(i);
            if (maisProxima != null) {
                atual.addCoins(maisProxima.getHalfCoins());
            }
        }
    }

    private Creature encontrarMaisProxima(int idx) {
        Creature atual = criaturas.get(idx);
        Creature maisProxima = null;
        double menorDist = Double.MAX_VALUE;

        for (int i = 0; i < criaturas.size(); i++) {
            if (i == idx) continue;
            Creature outra = criaturas.get(i);
            double dist = Math.abs(atual.getX() - outra.getX());
            if (dist < menorDist) {
                menorDist = dist;
                maisProxima = outra;
            }
        }
        return maisProxima;
    }

    public List<Creature> getCriaturas() {
        return criaturas;
    }
}


