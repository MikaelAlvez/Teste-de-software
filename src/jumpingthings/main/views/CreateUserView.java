package jumpingthings.main.views;

import jumpingthings.main.user.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Objects;

public class CreateUserView extends JPanel {
    private JTextField loginField;
    private JPasswordField passwordField;
    private JLabel avatarLabel;
    private File selectedAvatar;
    private final UserService userService;

    public CreateUserView(final UserService userService) {
        this.userService = userService;
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
            final var login = loginField.getText().trim();
            final var password = new String(passwordField.getPassword());
            final var avatarPath = selectedAvatar != null ? selectedAvatar.getAbsolutePath() : null;
            if (login.isEmpty() || password.isEmpty() || avatarPath == null) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Aqui você pode chamar o UserDAO para salvar o usuário
            try {
                // Verifica duplicidade de login
                final var loginAlreadyExists = this.userService.findUserByLogin(login);
                if (!loginAlreadyExists.login().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Login já existente!");
                    return;
                }

                // Cria Path de origem
                Path source = Path.of(avatarPath);

                // Extrai extensão
                String ext = "";
                int dotIndex = avatarPath.lastIndexOf('.');
                if (dotIndex >= 0) {
                    ext = avatarPath.substring(dotIndex); // exemplo: ".jpg"
                }

                // Cria pasta caso não exista
                Path destDir = Path.of("src", "resources");
                Files.createDirectories(destDir);

                // Caminho final: src/resources/login.ext
                Path target = destDir.resolve(login + ext);

                // Copia o arquivo
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

                // Chama serviço com caminho final salvo
                this.userService.createUser(login, password, target.toString());

                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
                RouterView.getInstance().navigateTo("/sign/in");

            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao salvar usuário no banco", ex);
            } catch (IOException ioEx) {
                JOptionPane.showMessageDialog(this, "Erro ao copiar avatar!", "Erro", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ioEx);
            }
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
