package jumpingthings.main.views;

import javax.swing.*;
import java.awt.*;

public class SignInView extends JPanel {
    private JTextField loginField;
    private JPasswordField passwordField;

    public SignInView() {
        startUp();
    }

    private void startUp() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel title = new JLabel("Login");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Campo de login
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Login:"), gbc);

        loginField = new JTextField(20);
        gbc.gridx = 1;
        add(loginField, gbc);

        // Campo de senha
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Senha:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Voltar");

        loginButton.addActionListener(e -> {
            String login = loginField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Aqui você colocaria verificação com UserDAO
            JOptionPane.showMessageDialog(this,
                    "Tentando login com:\nLogin: " + login + "\nSenha: " + password);
        });
        backButton.addActionListener(e ->RouterView.getInstance().navigateTo("/"));

        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }

    // Getters (se quiser usar em testes ou lógicas externas)
    public String getLogin() {
        return loginField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }
}
