package jumpingthings.main.views;


import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;
import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {
    private static final int TIMER_MS = 1000;
    private static final int MAXIMUM_MATCH_DURATION = 100;

    private MatchWithClusterAndGuardian match;
    private VisualizationPanelWithClusterAndGuardian panel;
    private Timer timer;
    private int counter = 0;

    public GameView() {
        startUp();
    }

    private void startUp() {
        setLayout(new BorderLayout());

        // Inicia a simulação
        match = new MatchWithClusterAndGuardian(30);
        panel = new VisualizationPanelWithClusterAndGuardian(match);
        add(panel, BorderLayout.CENTER);

        JButton backButton = new JButton("Sair");
        backButton.addActionListener(e -> {
            removeSimulation();
            RouterView.getInstance().navigateTo("/sign/in");
        });

        JPanel footer = new JPanel();
        footer.add(backButton);
        add(footer, BorderLayout.SOUTH);

        startSimulation();
    }

    public void restartSimulation() {
        if (timer != null) timer.stop();
        match.reset();
        counter = 0;
        timer.start();
    }

    private void removeSimulation() {
        if (timer != null) timer.stop();
        match.reset();
        counter = 0;
    }

    private void startSimulation() {
        counter = 0;
        timer = new Timer(TIMER_MS, e -> {
            match.iterate();
            panel.repaint();
            counter++;
            if (counter >= MAXIMUM_MATCH_DURATION || match.isFinished()) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Simulação encerrada.", "Fim", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        timer.start();
    }
}
