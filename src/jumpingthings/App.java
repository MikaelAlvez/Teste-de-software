package jumpingthings;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        Simulador simulador = new Simulador(4);
        PainelVisualizacao painel = new PainelVisualizacao(simulador);

        JFrame frame = new JFrame("Simulação de Criaturas Saltitantes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(painel);
        frame.pack();
        frame.setVisible(true);

        new Timer(1000, e -> {
            simulador.iterar();
            painel.repaint();
        }).start();
    }
}
