package jumpingthings.main.views;


import jumpingthings.main.App;
import jumpingthings.main.game.MatchWithClusterAndGuardian;
import jumpingthings.main.game.VisualizationPanelWithClusterAndGuardian;
import jumpingthings.main.user.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class GameView extends JPanel {
    private static final int TIMER_MS = 1000;
    private static final int MAXIMUM_MATCH_DURATION = 100;

    private MatchWithClusterAndGuardian match;
    private VisualizationPanelWithClusterAndGuardian panel;
    private Timer timer;
    private int counter = 0;
    private final UserService userService;

    public GameView(final UserService userService) {
        this.userService = userService;
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
            App.authenticated = null;
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
            if (counter >= MAXIMUM_MATCH_DURATION) {
              try {
                final var user = this.userService.findUserByLogin(App.authenticated);
                this.userService.updateScore(user.id(), user.score() + 10);
                this.userService.updateSimulationsRun(user.id(), user.simulationsRun() + 1);
                timer.stop();
                JOptionPane.showMessageDialog(this, "Simulação encerrada por limite de execução +10.", "Fim", JOptionPane.INFORMATION_MESSAGE);
              } catch (SQLException ex) {
                throw new RuntimeException(ex);
              }
            }
            if (match.isFinished()) {
              try {
                final var user = this.userService.findUserByLogin(App.authenticated);
                this.userService.updateScore(user.id(), user.score() + 100);
                this.userService.updateSimulationsRun(user.id(), user.simulationsRun() + 1);
                this.userService.updateSuccessfulSimulations(user.id(), user.successfulSimulations() + 1);
                timer.stop();
                JOptionPane.showMessageDialog(this, "Você GANHOU +100 pontos.", "Fim", JOptionPane.INFORMATION_MESSAGE);
              } catch (SQLException ex) {
                throw new RuntimeException(ex);
              }
            }
        });
        timer.start();
    }
}
