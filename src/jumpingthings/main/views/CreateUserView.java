package jumpingthings.main.views;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CreateUserView extends JPanel {
    private JTextField loginField;
    private JPasswordField passwordField;
    private JLabel avatarLabel;
    private File selectedAvatar;

    public CreateUserView() {
        startUp();
    }

    private void startUp() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginTitle = new JLabel("Cadastro de Usuário");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(loginTitle, gbc);

        gbc.gridwidth = 1;

        // Login
        JLabel loginLabel = new JLabel("Login:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(loginLabel, gbc);

        loginField = new JTextField(20);
        gbc.gridx = 1;
        add(loginField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Avatar
        JLabel avatarTextLabel = new JLabel("Avatar:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(avatarTextLabel, gbc);

        JPanel avatarPanel = new JPanel(new BorderLayout());
        JButton selectAvatarButton = new JButton("Selecionar Arquivo");
        avatarLabel = new JLabel("Nenhum arquivo selecionado");

        selectAvatarButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedAvatar = chooser.getSelectedFile();
                avatarLabel.setText(selectedAvatar.getName());
            }
        });

        avatarPanel.add(selectAvatarButton, BorderLayout.WEST);
        avatarPanel.add(avatarLabel, BorderLayout.CENTER);
        gbc.gridx = 1;
        add(avatarPanel, gbc);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton saveButton = new JButton("Salvar");
        JButton backButton = new JButton("Voltar");

        saveButton.addActionListener(e -> {
            String login = loginField.getText().trim();
            String password = new String(passwordField.getPassword());
            String avatar = selectedAvatar != null ? selectedAvatar.getAbsolutePath() : null;

            if (login.isEmpty() || password.isEmpty() || avatar == null) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Aqui você pode chamar o UserDAO para salvar o usuário
            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");

            // Após cadastro, pode navegar de volta para tela de login
            RouterView.getInstance().navigateTo("/sign/in");
        });

        backButton.addActionListener(e -> {
            RouterView.getInstance().navigateTo("/");
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);
    }

    // Getters (opcional, se quiser reutilizar os dados fora da tela)
    public String getLogin() {
        return loginField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public File getAvatarFile() {
        return selectedAvatar;
    }
}
