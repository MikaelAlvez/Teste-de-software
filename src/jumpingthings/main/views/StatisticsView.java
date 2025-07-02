package jumpingthings.main.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StatisticsView extends JPanel {

    public StatisticsView(List<UserStats> stats) {
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

        // Cálculos totais
        int totalSimulacoes = stats.stream().mapToInt(u -> u.total).sum();
        int totalSucessos = stats.stream().mapToInt(u -> u.success).sum();
        double mediaGeral = stats.isEmpty() ? 0 : ((double) totalSucessos / totalSimulacoes) * 100;

        // Monta dados
        Object[][] data = new Object[stats.size()][columns.length];
        for (int i = 0; i < stats.size(); i++) {
            UserStats u = stats.get(i);
            double mediaUsuario = u.total == 0 ? 0 : ((double) u.success / u.total) * 100;

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