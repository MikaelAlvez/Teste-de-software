package jumpingthings.main.views;

import jumpingthings.main.user.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatisticsView extends JPanel {
    private final UserService userService;

    public StatisticsView(final UserService userService) {
        this.userService = userService;
        startUp();
    }

    private void startUp() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Estatísticas de Jogadores", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Dados da tabela
        String[] columns = {
                "Login",
                "Simulações Executadas",
                "Bem-Sucedidas",
                "Média Usuário (%)",
                "Total Simulações",
                "Média Total (%)"
        };


        List<UserStats> usersStats = new ArrayList<>();
        try {
            final var users = this.userService.getAllUsers();
            for (final var user : users) usersStats.add(new UserStats(user.login(), user.simulationsRun(), user.successfulSimulations()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Cálculos totais
        final var totalSimulacoes = usersStats.stream().mapToInt(u -> u.total).sum();
        final var totalSucessos = usersStats.stream().mapToInt(u -> u.success).sum();
        final var mediaGeral = (usersStats.isEmpty() || totalSimulacoes == 0) ? 0 : ((double) totalSucessos / totalSimulacoes) * 100;

        // Monta dados
        final var data = new Object[usersStats.size()][columns.length];
        for (int i = 0; i < usersStats.size(); i++) {
            final var u = usersStats.get(i);
            final var mediaUsuario = u.total == 0 ? 0 : ((double) u.success / u.total) * 100;
            data[i] = new Object[]{
                    u.login,
                    u.total,
                    u.success,
                    String.format("%.2f%%", mediaUsuario),
                    totalSimulacoes,
                    String.format("%.2f%%", mediaGeral)
            };
        }

        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JButton backButton = new JButton("Voltar");
        backButton.addActionListener(e -> RouterView.getInstance().navigateTo("/"));
        JPanel footer = new JPanel();
        footer.add(backButton);
        add(footer, BorderLayout.SOUTH);
    }

    // Classe auxiliar para representar os dados por usuário
        public record UserStats(String login, int total, int success) {
    }
}