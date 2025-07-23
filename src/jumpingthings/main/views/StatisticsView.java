package jumpingthings.main.views;

import jumpingthings.main.App;
import jumpingthings.main.user.protocols.UserServiceInterface;
import jumpingthings.main.user.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatisticsView extends JPanel {
    private final UserServiceInterface userService;

    public StatisticsView(final UserServiceInterface userService) {
        this.userService = userService;
        startUp();
    }

    private void startUp() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Estatísticas de Jogadores", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Cabeçalho das colunas da tabela (sem imagem ainda)
        String[] columns = {
                "Login",
                "Pontos",
                "Simulações Executadas",
                "Bem-Sucedidas",
                "Média Usuário (%)",
                "Total Simulações",
                "Média Total (%)"
        };

        int score = 0;
        List<UserStats> usersStats = new ArrayList<>();
        try {
            final var users = this.userService.getAllUsers();
            final var userLogged = this.userService.findUserByLogin(App.authenticated);
            for (final var user : users) {
                usersStats.add(new UserStats(user.avatar(), user.login(), user.score(), user.simulationsRun(), user.successfulSimulations()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        final var totalSimulacoes = usersStats.stream().mapToInt(u -> u.total).sum();
        final var totalSucessos = usersStats.stream().mapToInt(u -> u.success).sum();
        final var mediaGeral = (usersStats.isEmpty() || totalSimulacoes == 0) ? 0 : ((double) totalSucessos / totalSimulacoes) * 100;

        // Monta dados
        final var data = new Object[usersStats.size()][columns.length + 1]; // +1 para a imagem
        for (int i = 0; i < usersStats.size(); i++) {
            final var u = usersStats.get(i);
            final var mediaUsuario = u.total == 0 ? 0 : ((double) u.success / u.total) * 100;

            // Avatar como ImageIcon
            ImageIcon avatarIcon = new ImageIcon(u.avatar); // ajuste a extensão se necessário
            Image image = avatarIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // redimensiona
            avatarIcon = new ImageIcon(image);

            data[i] = new Object[]{
                    avatarIcon, // Coluna 0: imagem
                    u.login,
                    u.score,
                    u.total,
                    u.success,
                    String.format("%.2f%%", mediaUsuario),
                    totalSimulacoes,
                    String.format("%.2f%%", mediaGeral)
            };
        }

        String[] fullColumns = new String[data[0].length];
        fullColumns[0] = "Avatar";
        System.arraycopy(columns, 0, fullColumns, 1, columns.length);

        // Cria a tabela com renderizador personalizado para imagens
        JTable table = new JTable(new DefaultTableModel(data, fullColumns)) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return ImageIcon.class;
                return Object.class;
            }
        };

        table.setRowHeight(60);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JButton backButton = new JButton("Voltar");
        backButton.setName("StatisticsView.backButton");
        backButton.addActionListener(e -> RouterView.getInstance().navigateTo("/"));
        JPanel footer = new JPanel();
        footer.add(backButton);
        add(footer, BorderLayout.SOUTH);
    }

//    private void startUp() {
//        setLayout(new BorderLayout());
//
//        JLabel title = new JLabel("Estatísticas de Jogadores", SwingConstants.CENTER);
//        title.setFont(new Font("Arial", Font.BOLD, 18));
//        add(title, BorderLayout.NORTH);
//
//        // Dados da tabela
//        String[] columns = {
//                "Login",
//                "Pontos",
//                "Simulações Executadas",
//                "Bem-Sucedidas",
//                "Média Usuário (%)",
//                "Total Simulações",
//                "Média Total (%)"
//        };
//
//        int score = 0;
//        List<UserStats> usersStats = new ArrayList<>();
//        try {
//            final var users = this.userService.getAllUsers();
//            final var userLogged = this.userService.findUserByLogin(App.authenticated);
//            for (final var user : users) usersStats.add(new UserStats(user.login(), user.score(), user.simulationsRun(), user.successfulSimulations()));
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        // Cálculos totais
//        final var totalSimulacoes = usersStats.stream().mapToInt(u -> u.total).sum();
//        final var totalSucessos = usersStats.stream().mapToInt(u -> u.success).sum();
//        final var mediaGeral = (usersStats.isEmpty() || totalSimulacoes == 0) ? 0 : ((double) totalSucessos / totalSimulacoes) * 100;
//
//        // Monta dados
//        final var data = new Object[usersStats.size()][columns.length];
//        for (int i = 0; i < usersStats.size(); i++) {
//            final var u = usersStats.get(i);
//            final var mediaUsuario = u.total == 0 ? 0 : ((double) u.success / u.total) * 100;
//            data[i] = new Object[]{
//                    u.login,
//                    u.score,
//                    u.total,
//                    u.success,
//                    String.format("%.2f%%", mediaUsuario),
//                    totalSimulacoes,
//                    String.format("%.2f%%", mediaGeral)
//            };
//        }
//
//        JTable table = new JTable(new DefaultTableModel(data, columns));
//        JScrollPane scroll = new JScrollPane(table);
//        add(scroll, BorderLayout.CENTER);
//
//        JButton backButton = new JButton("Voltar");
//        backButton.addActionListener(e -> RouterView.getInstance().navigateTo("/"));
//        JPanel footer = new JPanel();
//        footer.add(backButton);
//        add(footer, BorderLayout.SOUTH);
//    }

    // Classe auxiliar para representar os dados por usuário
        public record UserStats(String avatar, String login, int score, int total, int success) {
    }
}