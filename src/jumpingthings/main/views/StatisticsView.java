package jumpingthings.main.views;

import jumpingthings.main.App;
import jumpingthings.main.user.protocols.UserServiceInterface;
import jumpingthings.main.user.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StatisticsView extends JPanel {
    private final UserServiceInterface userService;
    private DefaultTableModel tableModel; // <-- Tabela como variável de instância
    private JTable table; // <-- Tabela como variável de instância

    public StatisticsView(final UserServiceInterface userService) {
        this.userService = userService;
        startUp();
    }

    private void startUp() {
        setLayout(new BorderLayout());

        // 1. CRIAÇÃO DOS COMPONENTES (FEITO APENAS UMA VEZ)
        JLabel title = new JLabel("Estatísticas de Jogadores", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Define as colunas
        String[] columns = {
                "Avatar",
                "Login",
                "Pontos",
                "Simulações Executadas",
                "Bem-Sucedidas",
                "Média Usuário (%)",
                "Total Simulações",
                "Média Total (%)"
        };

        // Cria o modelo da tabela VAZIO
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return ImageIcon.class; // Para renderizar a imagem
                return Object.class;
            }
        };

        // Cria a JTable com o modelo
        table = new JTable(tableModel);
        table.setRowHeight(40); // Ajuste a altura da linha conforme necessário
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // Cria o painel de rodapé com os botões
        JPanel footer = new JPanel();
        JButton backButton = new JButton("Voltar");
        backButton.setName("StatisticsView.backButton");
        JButton reloadButton = new JButton("Recarregar");
        reloadButton.setName("StatisticsView.reloadButton");

        footer.add(backButton);
        footer.add(reloadButton);
        add(footer, BorderLayout.SOUTH);

        // 2. AÇÕES DOS BOTÕES
        backButton.addActionListener(e -> RouterView.getInstance().navigateTo("/"));
        reloadButton.addActionListener(e -> reloadData()); // <-- Ação para recarregar

        // 3. CARREGA OS DADOS PELA PRIMEIRA VEZ
        reloadData();
    }

    /**
     * Limpa a tabela, busca os dados mais recentes do banco de dados
     * e preenche a tabela novamente.
     */
    private void reloadData() {
        // Limpa os dados antigos da tabela
        tableModel.setRowCount(0);

        List<UserStats> usersStats = new ArrayList<>();
        try {
            // Busca os dados atualizados
            final var users = this.userService.getAllUsers();
            for (final var user : users) {
                usersStats.add(new UserStats(user.avatar(), user.login(), user.score(), user.simulationsRun(), user.successfulSimulations()));
            }
        } catch (SQLException e) {
            // Em caso de erro, exibe uma mensagem e interrompe
            JOptionPane.showMessageDialog(this, "Erro ao carregar estatísticas: " + e.getMessage(), "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }

        // Cálculos totais
        final var totalSimulacoes = usersStats.stream().mapToInt(u -> u.total).sum();
        final var totalSucessos = usersStats.stream().mapToInt(u -> u.success).sum();
        final var mediaGeral = (usersStats.isEmpty() || totalSimulacoes == 0) ? 0 : ((double) totalSucessos / totalSimulacoes) * 100;

        // Preenche a tabela com os novos dados
        usersStats.sort(Comparator.comparingInt(UserStats::score).reversed());
        for (final var u : usersStats) {
            final var mediaUsuario = u.total == 0 ? 0 : ((double) u.success / u.total) * 100;

            // Avatar como ImageIcon
            ImageIcon avatarIcon = new ImageIcon(u.avatar);
            Image image = avatarIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            avatarIcon = new ImageIcon(image);

            Object[] rowData = {
                    avatarIcon,
                    u.login,
                    u.score,
                    u.total,
                    u.success,
                    String.format("%.2f%%", mediaUsuario),
                    totalSimulacoes,
                    String.format("%.2f%%", mediaGeral)
            };
            tableModel.addRow(rowData); // <-- Adiciona linha ao modelo existente
        }
    }

    // Classe auxiliar para representar os dados por usuário
    public record UserStats(String avatar, String login, int score, int total, int success) {
    }



        // SEM RELOAD
//    private final UserServiceInterface userService;
//
//    public StatisticsView(final UserServiceInterface userService) {
//        this.userService = userService;
//        startUp();
//    }
//
//    private void startUp() {
//        setLayout(new BorderLayout());
//
//        JLabel title = new JLabel("Estatísticas de Jogadores", SwingConstants.CENTER);
//        title.setFont(new Font("Arial", Font.BOLD, 18));
//        add(title, BorderLayout.NORTH);
//
//        // Cabeçalho das colunas da tabela (sem imagem ainda)
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
//            for (final var user : users) {
//                usersStats.add(new UserStats(user.avatar(), user.login(), user.score(), user.simulationsRun(), user.successfulSimulations()));
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        final var totalSimulacoes = usersStats.stream().mapToInt(u -> u.total).sum();
//        final var totalSucessos = usersStats.stream().mapToInt(u -> u.success).sum();
//        final var mediaGeral = (usersStats.isEmpty() || totalSimulacoes == 0) ? 0 : ((double) totalSucessos / totalSimulacoes) * 100;
//
//        // Monta dados
//        final var data = new Object[usersStats.size()][columns.length + 1]; // +1 para a imagem
//        for (int i = 0; i < usersStats.size(); i++) {
//            final var u = usersStats.get(i);
//            final var mediaUsuario = u.total == 0 ? 0 : ((double) u.success / u.total) * 100;
//
//            // Avatar como ImageIcon
//            ImageIcon avatarIcon = new ImageIcon(u.avatar); // ajuste a extensão se necessário
//            Image image = avatarIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // redimensiona
//            avatarIcon = new ImageIcon(image);
//
//            data[i] = new Object[]{
//                    avatarIcon, // Coluna 0: imagem
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
//        if (!usersStats.isEmpty()) {
//            String[] fullColumns = new String[data[0].length];
//            fullColumns[0] = "Avatar";
//            System.arraycopy(columns, 0, fullColumns, 1, columns.length);
//
//            // Cria a tabela com renderizador personalizado para imagens
//            JTable table = new JTable(new DefaultTableModel(data, fullColumns)) {
//                @Override
//                public Class<?> getColumnClass(int column) {
//                    if (column == 0) return ImageIcon.class;
//                    return Object.class;
//                }
//            };
//
//            table.setRowHeight(60);
//            JScrollPane scroll = new JScrollPane(table);
//            add(scroll, BorderLayout.CENTER);
//
//        }
//
//        JButton backButton = new JButton("Voltar");
//        backButton.addActionListener(e -> RouterView.getInstance().navigateTo("/"));
//        JPanel footer = new JPanel();
//        footer.add(backButton);
//        add(footer, BorderLayout.SOUTH);
//    }
    // Classe auxiliar para representar os dados por usuário
        //public record UserStats(String avatar, String login, int score, int total, int success) {
    //}
}