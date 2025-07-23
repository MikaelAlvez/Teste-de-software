package jumpingthings.main.game;

import jumpingthings.main.common.Env;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Comparator;

public class VisualizationPanelWithClusterAndGuardian extends JPanel {
    private final MatchWithClusterAndGuardian match;

    public VisualizationPanelWithClusterAndGuardian(final MatchWithClusterAndGuardian match) {
        this.match = Objects.requireNonNull(match);
        setPreferredSize(new Dimension(Env.WINDOW_WIDTH, Env.WINDOW_HEIGHT));
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int margin = 50;
        int lineY = panelHeight - 50;

        // Linha base do horizonte
        g.setColor(Color.BLACK);
        g.drawLine(margin, lineY, panelWidth - margin, lineY);

        long currentTime = System.currentTimeMillis();

        drawCreatures(g, currentTime, panelWidth, lineY, margin);
        if (!match.getClusters().isEmpty()) drawClusters(g, panelWidth, lineY, margin);
        drawGuardian(g, panelWidth, lineY, margin);
        drawInfoBox(g, panelWidth);
    }

    // === Desenha Criaturas Saltitantes ===
    private void drawCreatures(Graphics g, long currentTime, int panelWidth, int lineY, int margin) {
        for (final var creature : match.getCreatures()) {
            double normalizedX = normalizePositionX(creature.getX());
            int x = (int) (margin + normalizedX * (panelWidth - 2 * margin));
            double frequency = 0.005;
            int jumpAmplitude = 40;
            int yOffset = (int) (Math.abs(Math.sin(currentTime * frequency + creature.getId())) * jumpAmplitude);
            int y = lineY - 12 - yOffset;

            g.setColor(Color.BLUE);
            g.fillOval(x, y, 10, 10);
            g.setColor(Color.BLACK);
            g.drawString("ID: " + creature.getId(), x + 15, y + 10);
        }
    }

    // === Desenha Clusters ===
    private void drawClusters(Graphics g, int panelWidth, int lineY, int margin) {
        for (final var cluster : match.getClusters()) {
            double normalizedX = normalizePositionX(cluster.getGameplay().getX());
            int x = (int) (margin + normalizedX * (panelWidth - 2 * margin));
            int y = lineY - 20;

            g.setColor(Color.RED);
            g.fillOval(x - 5, y, 14, 14);
            g.setColor(Color.BLACK);
            g.drawString("CL " + cluster.getId(), x + 15, y + 10);
        }
    }

    // === Desenha o Guardião do Horizonte ===
    private void drawGuardian(Graphics g, int panelWidth, int lineY, int margin) {
        double normalizedX = normalizePositionX(match.getGuardian().getGameplay().getX());
        int x = (int) (margin + normalizedX * (panelWidth - 2 * margin));
        int y = lineY - 30;

        g.setColor(new Color(128, 0, 128)); // roxo
        g.fillOval(x, y, 14, 14);
        g.setColor(Color.BLACK);
        g.drawString("GUARD", x + 15, y + 10);
    }

    // === Painel com as moedas de todas as entidades ===
    private void drawInfoBox(Graphics g, int panelWidth) {
        int infoBoxWidth = 180;
        int lineHeight = 15;
        int padding = 10;
        int x = panelWidth - infoBoxWidth - padding;
        int textX = x + padding;
        int textY = padding + lineHeight * 2;

        int height = (match.getCreatures().size() + match.getClusters().size() + 4) * lineHeight;
        g.setColor(new Color(240, 240, 240, 220));
        g.fillRect(x, padding, infoBoxWidth, height);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x, padding, infoBoxWidth, height);

        g.setColor(Color.BLACK);
        g.drawString("=== Moedas ===", textX, textY - lineHeight);

        // Criaturas
        g.setColor(Color.BLUE);
        g.drawString("Criaturas:", textX, textY);
        textY += lineHeight;
        List<Creature> sortedCreatures = new ArrayList<>(match.getCreatures());
        sortedCreatures.sort(Comparator.comparingInt(Creature::getCoins).reversed());
        for (final var c : sortedCreatures) {
            g.drawString("ID " + c.getId() + ": " + formatCoins(c.getCoins()), textX, textY);
            textY += lineHeight;
        }

       if (!match.getClusters().isEmpty()) {
           // Clusters
           g.setColor(Color.RED);
           g.drawString("Clusters:", textX, textY);
           textY += lineHeight;
           List<Cluster> sortedClusters = new ArrayList<>(match.getClusters());
           sortedClusters.sort(Comparator.comparingInt(cl -> ((Cluster) cl).getGameplay().getCoins()).reversed());
           for (final var cluster : sortedClusters) {
               g.drawString("CL " + cluster.getId() + ": " + formatCoins(cluster.getGameplay().getCoins()), textX, textY);
               textY += lineHeight;
           }
       }

        // Guardião
        g.setColor(new Color(128, 0, 128));
        g.drawString("Guardião:", textX, textY);
        textY += lineHeight;
        g.drawString("ID 110: " + formatCoins(match.getGuardian().getGameplay().getCoins()), textX, textY);
    }

    // === Normaliza X de [-1, 1] para [0.0, 1.0] ===
    public double normalizePositionX(final float positionX) {
        return (positionX + 1) / 2.0;
    }

    // === Formata moedas para o estilo brasileiro ===
    private String formatCoins(final int coins) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.of("pt", "BR"));
        format.setMaximumFractionDigits(0);
        return format.format(coins);
    }
}
