package jumpingthings.main.views;

import jumpingthings.main.user.protocols.UserServiceInterface;
import jumpingthings.main.user.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DeleteUserView extends JPanel {
    private JTextField loginField;
    private final UserServiceInterface userService;

    public DeleteUserView(final UserServiceInterface userService) {
        this.userService = userService;
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
        loginField.setName("DeleteUserView.loginButton");
        gbc.gridx = 1;
        add(loginField, gbc);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton deleteButton = new JButton("Deletar");
        deleteButton.setName("DeleteUserView.deleteButton");
        JButton backButton = new JButton("Voltar");
        backButton.setName("DeleteUserView.backButton");

        deleteButton.addActionListener(e -> {
            final var login = loginField.getText().trim();
            if (login.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o login do usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isDeleted;
            try {
                isDeleted = this.userService.deleteUserByLogin(login);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            if (isDeleted) {
                JOptionPane.showMessageDialog(this, "Usuário deletado com sucesso.");
                loginField.setText("");
                RouterView.getInstance().navigateTo("/");
            } else {
                JOptionPane.showMessageDialog(this, "Usuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> RouterView.getInstance().navigateTo("/"));

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
