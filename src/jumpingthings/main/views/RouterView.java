package jumpingthings.main.views;

import jumpingthings.main.common.Env;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RouterView extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final Map<String, JPanel> views;

    private static RouterView instance; // Singleton

    public RouterView() {
        setTitle("Aplicação");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //setPreferredSize(new Dimension(Env.WINDOW_WIDTH, Env.WINDOW_HEIGHT));
        setSize(Env.WINDOW_WIDTH, Env.WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        views = new HashMap<>();

        add(cardPanel);
    }

    // Singleton para acesso global
    public static RouterView getInstance() {
        if (instance == null) {
            instance = new RouterView();
        }
        return instance;
    }

    // Método para mostrar a janela principal
    public static void setAsMainView(String initialView) {
        RouterView router = getInstance();
        router.setVisible(true);
        router.navigateTo(initialView);
    }

    // Adiciona uma nova tela
    public void addView(String name, JPanel view) {
        views.put(name, view);
        cardPanel.add(view, name);
    }

    // Remove uma tela existente
    public void removeView(String name) {
        JPanel view = views.remove(name);
        if (view != null) {
            cardPanel.remove(view);
        }
    }

    // Navega para uma tela pelo nome
    public void navigateTo(String name) {
        if (views.containsKey(name)) {
            final var view = views.get(name);
            if (view instanceof GameView gameview) gameview.restartSimulation();
            cardLayout.show(cardPanel, name);
        } else {
            System.err.println("View '" + name + "' não existe.");
        }
    }
}
