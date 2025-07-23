package jumpingthings.main.views;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JPanel {

    public HomeView() {
        startUp();
    }


    private void startUp() {
        setLayout(new BorderLayout());
        // Painel com FlowLayout para evitar expansão dos botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        Dimension smallButton = new Dimension(120, 30); // largura, altura

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(smallButton);
        loginButton.setName("HomeView.loginButton");

        JButton createAccountButton = new JButton("Criar Conta");
        createAccountButton.setPreferredSize(smallButton);
        createAccountButton.setName("HomeView.createAccountButton");

        JButton deleteAccountButton = new JButton("Excluir Conta");
        deleteAccountButton.setPreferredSize(smallButton);
        deleteAccountButton.setName("HomeView.deleteAccountButton");

        JButton statisticsButton = new JButton("Ver Estatísticas");
        statisticsButton.setPreferredSize(smallButton);
        statisticsButton.setName("HomeView.statsButton");

        JButton aboutButton = new JButton("Sobre");
        aboutButton.setPreferredSize(smallButton);
        aboutButton.setName("HomeView.aboutButton");

        // Ações
        loginButton.addActionListener(e -> RouterView.getInstance().navigateTo("/sign/in"));
        createAccountButton.addActionListener(e -> RouterView.getInstance().navigateTo("/create/user"));
        deleteAccountButton.addActionListener(e -> RouterView.getInstance().navigateTo("/delete/user"));
        statisticsButton.addActionListener(e -> RouterView.getInstance().navigateTo("/statistics"));
        aboutButton.addActionListener(e -> RouterView.getInstance().navigateTo("/about"));

        // Adiciona ao painel
        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);
        buttonPanel.add(deleteAccountButton);
        buttonPanel.add(statisticsButton);
        buttonPanel.add(aboutButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

}
