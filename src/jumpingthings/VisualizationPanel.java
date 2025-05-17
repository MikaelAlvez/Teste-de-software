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

        // Linha horizontal no meio
        int lineY = panelHeight - 50;
        graphics.setColor(Color.BLACK);
        graphics.drawLine(margin, lineY, panelWidth - margin, lineY);

        final long currentTime = System.currentTimeMillis(); // tempo atual para animar o salto

        final var creatures = match.getCreatures();

        for (final var creature : creatures) {
            // Escala valor de -1 a 1 para a largura do painel
            final double normalizedPosition = normalizesCreaturePositionScreen(creature.getX()); // de [-1, 1] para [0, 1]
            final int x = (int) (margin + normalizedPosition * (panelWidth - 2 * margin));

            // Oscilação vertical em forma de salto (seno do tempo)
            final double frequency = 0.005; // menor = mais lento
            final int jumpAmplitude = 40; // altura máxima do salto
            // int yOffset = (int)(Math.sin(currentTime * frequency + creature.getId()) * jumpAmplitude);
            final int yOffset = (int)(Math.abs(Math.sin(currentTime * frequency + creature.getId())) * jumpAmplitude);
            final int y = lineY - 12 - yOffset; // aplica o salto
            graphics.setColor(Color.BLUE);
            graphics.fillOval(x, y, 10, 10);
            graphics.setColor(Color.BLACK);
            graphics.drawString("ID: " + creature.getId(), x + 15, y + 10);
        }

        // Desenha painel no canto superior direito mostrando as moedas das criaturas
        final var infoBoxWidth = 150;
        final var lineHeight = 15;
        final var padding = 10;
        final var infoBoxX = panelWidth - infoBoxWidth - padding;
        final var infoBoxHeight = creatures.size() * lineHeight + 2 * padding;

        // Retângulo de fundo
        graphics.setColor(new Color(240, 240, 240, 220)); // cinza claro com leve transparência
        graphics.fillRect(infoBoxX, padding, infoBoxWidth, infoBoxHeight);

        // Borda
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawRect(infoBoxX, padding, infoBoxWidth, infoBoxHeight);

        // Texto
        graphics.setColor(Color.BLACK);
        final var textX = infoBoxX + padding;
        var textY = padding + padding + lineHeight;

        // Ordena criaturas por moedas (ordem decrescente)
        final var sortedCreatures = new ArrayList<>(creatures);
        sortedCreatures.sort(Comparator.comparingInt(Creature::getCoins).reversed());

        for (final var creature : sortedCreatures) {
            graphics.drawString("ID " + creature.getId() + ": " + formatterCoins(creature.getCoins()) + " coins", textX, textY);
            textY += lineHeight;
        }
    }

    /**
     * Normaliza a posição horizontal de uma criatura no eixo X, convertendo um valor
     * que originalmente varia de -1 a 1 para o intervalo [0.0, 1.0].
     * <p>
     * Esse tipo de normalização é útil em simulações ou animações onde o espaço lógico
     * das entidades vai de -1 (extrema esquerda) até 1 (extrema direita), mas o painel
     * gráfico utiliza coordenadas absolutas em pixels e dentro de uma área útil limitada.
     * </p>
     * <p>
     * A fórmula aplicada é: {@code (positionX + 1) / 2.0}, que transforma:
     * <ul>
     *     <li>{@code -1} → {@code 0.0} (início da área útil, geralmente após a margem esquerda)</li>
     *     <li>{@code  0} → {@code 0.5} (meio da área útil)</li>
     *     <li>{@code  1} → {@code 1.0} (fim da área útil, antes da margem direita)</li>
     * </ul>
     * </p>
     *
     * @param positionX A posição X da criatura, em uma escala normalizada de -1 a 1.
     * @return A posição X normalizada entre 0.0 e 1.0, pronta para ser convertida em coordenadas de tela.
     */
    private double normalizesCreaturePositionScreen(final float positionX) {
        return (positionX + 1) / 2.0;
    }

    /**
     * Formata a quantidade de moedas (inteiro) de uma criatura para o padrão numérico brasileiro.
     * <p>
     * Utiliza o {@link NumberFormat} com a localidade "pt-BR" (português do Brasil),
     * garantindo que os números sejam exibidos com separadores de milhar apropriados,
     * sem casas decimais.
     * </p>
     * <p>
     * Exemplo:
     * <ul>
     *     <li>{@code 1000} → {@code "1.000"}</li>
     *     <li>{@code 2500000} → {@code "2.500.000"}</li>
     * </ul>
     * </p>
     *
     * @param coins A quantidade de moedas da criatura.
     * @return Uma string formatada com o número de moedas no estilo brasileiro, sem decimais.
     */
    private String formatterCoins(final int coins) {
        NumberFormat brCurrency = NumberFormat.getNumberInstance(Locale.of("pt", "BR"));
        brCurrency.setMaximumFractionDigits(0);
        return brCurrency.format(coins);
    }
}
