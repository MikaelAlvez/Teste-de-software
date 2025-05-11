package jumpingthings;

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

        int larguraPainel = getWidth();
        int alturaPainel = getHeight();
        int margem = 50;

        // Linha horizontal no meio
        int linhaY = alturaPainel / 2;
        g.setColor(Color.GRAY);
        g.drawLine(margem, linhaY, larguraPainel - margem, linhaY);

        long tempo = System.currentTimeMillis(); // tempo atual para animar pulo

        List<Creature> criaturas = simulador.getCriaturas();

        for (Creature c : criaturas) {
            // Escalar valor de -1 a 1 para a largura do painel
            double posicaoNormalizada = (c.getX() + 1) / 2.0; // de [-1, 1] para [0, 1]
            int x = (int) (margem + posicaoNormalizada * (larguraPainel - 2 * margem));

            // Oscilação vertical em forma de pulo (seno de tempo)
            double frequencia = 0.005; // menor = mais devagar
            int amplitudePulo = 10; // altura máxima do pulo
            int deslocamentoY = (int)(Math.sin(tempo * frequencia + c.getId()) * amplitudePulo);

            int y = linhaY - 5 - deslocamentoY; // aplica o pulo

            g.setColor(Color.BLUE);
            g.fillOval(x, y, 10, 10);

            g.setColor(Color.BLACK);
            g.drawString("ID: " + c.getId() + " Ouro: " + c.getX(), x + 15, y + 10);
        }
    }
}


