package jumpingthings.main.game;

import javax.swing.*;

public class AppWithClusterAndGuardian {
    private final static int TIMER_MS = 1000;
    private final static int MAXIMUM_MATCH_DURATION = 100;

    public static void main(String[] args) {
        final var match = new MatchWithClusterAndGuardian(30);
        final var panel = new VisualizationPanelWithClusterAndGuardian(match);
        final var frame = new JFrame("Simulação com Guardião do Horizonte");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        final Timer[] timer = new Timer[1];
        final int[] counter = {0};

        timer[0] = new Timer(TIMER_MS, e -> {
            match.iterate();
            panel.repaint();
            counter[0]++;
            if (counter[0] >= MAXIMUM_MATCH_DURATION || match.isFinished()) {
                timer[0].stop();
                JOptionPane.showMessageDialog(frame, "Simulação encerrada.", "Fim", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        timer[0].start();
    }
}
