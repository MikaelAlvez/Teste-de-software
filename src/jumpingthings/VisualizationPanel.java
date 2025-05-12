package com.lucassf2k.main.jumpingthings;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class VisualizationPanel extends JPanel {
    private final Match match;

    public VisualizationPanel(final Match match) {
        this.match = Objects.requireNonNull(match);
        setPreferredSize(new Dimension(800, 600));
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int margin = 50;

        // Horizontal line in the middle
        int lineY = panelHeight / 2;
        graphics.setColor(Color.GRAY);
        graphics.drawLine(margin, lineY, panelWidth - margin, lineY);

        long currentTime = System.currentTimeMillis(); // current time to animate jump

        final var creatures = match.getCreatures();

        for (final var creature : creatures) {
            // Scale value from -1 to 1 to the panel width
            double normalizedPosition = (creature.getX() + 1) / 2.0; // from [-1, 1] to [0, 1]
            int x = (int) (margin + normalizedPosition * (panelWidth - 2 * margin));

            // Vertical oscillation in jump form (sine of time)
            double frequency = 0.005; // smaller = slower
            int jumpAmplitude = 20; // max jump height
            //int yOffset = (int)(Math.sin(currentTime * frequency + creature.getId()) * jumpAmplitude);
            int yOffset = (int)(Math.abs(Math.sin(currentTime * frequency + creature.getId())) * jumpAmplitude);
            int y = lineY - 5 - yOffset; // apply jump
            graphics.setColor(Color.BLUE);
            graphics.fillOval(x, y, 10, 10);
            graphics.setColor(Color.BLACK);
            graphics.drawString("ID: " + creature.getId(), x + 15, y + 10);
        }

        // Draw top-right panel showing creature coins
        final var infoBoxWidth = 150;
        final var lineHeight = 15;
        final var padding = 10;
        final var infoBoxX = panelWidth - infoBoxWidth - padding;
        final var infoBoxHeight = creatures.size() * lineHeight + 2 * padding;
// Background rectangle
        graphics.setColor(new Color(240, 240, 240, 220)); // light gray with slight transparency
        graphics.fillRect(infoBoxX, padding, infoBoxWidth, infoBoxHeight);
// Border
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawRect(infoBoxX, padding, infoBoxWidth, infoBoxHeight);
// Text
        graphics.setColor(Color.BLACK);
        final var textX = infoBoxX + padding;
        var textY = padding + padding + lineHeight;
// Sort creatures by coins descending
        final var sortedCreatures = new ArrayList<>(creatures);
        sortedCreatures.sort(Comparator.comparingInt(Creature::getCoins).reversed());

        for (final var creature : sortedCreatures) {
            graphics.drawString("ID " + creature.getId() + ": " + formatterCoins(creature.getCoins()) + " coins", textX, textY);
            textY += lineHeight;
        }
    }

    private String formatterCoins(final int coins) {
        NumberFormat brCurrency = NumberFormat.getNumberInstance(Locale.of("pt", "BR"));
        brCurrency.setMaximumFractionDigits(0);
        return brCurrency.format(coins);
    }
}
