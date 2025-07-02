package jumpingthings.main.views;

import javax.swing.*;
import java.awt.*;

public class DeleteUserView extends JPanel {
    private JTextField loginField;

    public DeleteUserView() {
        startUp();
    }

    private void startUp() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel title = new JLabel("Deletar Usuário");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Campo login
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Login:"), gbc);

        loginField = new JTextField(20);
        gbc.gridx = 1;
        add(loginField, gbc);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton deleteButton = new JButton("Deletar");
        JButton backButton = new JButton("Voltar");

        deleteButton.addActionListener(e -> {
            String login = loginField.getText().trim();
            if (login.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o login do usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Aqui seria a chamada para deletar o usuário via UserDAO
            // Exemplo fictício:
            // boolean deleted = new UserDAO().deleteByLogin(login);
            boolean deleted = true; // simulação

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Usuário deletado com sucesso.");
                loginField.setText("");
                RouterView.getInstance().navigateTo("/sign/in");
            } else {
                JOptionPane.showMessageDialog(this, "Usuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            RouterView.getInstance().navigateTo("signin");
        });

        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }

    public String getLogin() {
        return loginField.getText().trim();
    }
}
