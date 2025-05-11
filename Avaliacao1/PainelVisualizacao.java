package Avaliação1;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelVisualizacao extends JPanel {
    private final Simulador simulador;

    public PainelVisualizacao(Simulador simulador) {
        this.simulador = simulador;
        setPreferredSize(new Dimension(800, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        List<Criatura> criaturas = simulador.getCriaturas();
        for (Criatura c : criaturas) {
            int x = (int)(400 + c.getXi() / 1_000_000); // escala
            int y = c.getId() * 20 + 50;
            g.setColor(Color.BLUE);
            g.fillOval(x, y, 10, 10);
            g.drawString("ID: " + c.getId() + " Ouro: " + (long)c.getGi(), x + 15, y + 10);
        }
    }
}

