package Avaliação1;

import java.util.*;

public class Simulador {
    private List<Criatura> criaturas;

    public Simulador(int n) {
        criaturas = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            criaturas.add(new Criatura(i + 1));
        }
    }

    public void iterar() {
        for (Criatura c : criaturas) {
            c.atualizarPosicao();
        }

        for (int i = 0; i < criaturas.size(); i++) {
            Criatura atual = criaturas.get(i);
            Criatura maisProxima = encontrarMaisProxima(i);
            if (maisProxima != null) {
                double roubado = maisProxima.getGi() / 2.0;
                maisProxima.removeOuro(roubado);
                atual.addOuro(roubado);
            }
        }
    }

    private Criatura encontrarMaisProxima(int idx) {
        Criatura atual = criaturas.get(idx);
        Criatura maisProxima = null;
        double menorDist = Double.MAX_VALUE;

        for (int i = 0; i < criaturas.size(); i++) {
            if (i == idx) continue;
            Criatura outra = criaturas.get(i);
            double dist = Math.abs(atual.getXi() - outra.getXi());
            if (dist < menorDist) {
                menorDist = dist;
                maisProxima = outra;
            }
        }
        return maisProxima;
    }

    public List<Criatura> getCriaturas() {
        return criaturas;
    }
}

