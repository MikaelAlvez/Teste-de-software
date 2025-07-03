package jumpingthings.main.user.service;

import jumpingthings.main.user.model.User;
import jumpingthings.main.user.protocols.UserDAOInterface;
import jumpingthings.main.user.protocols.UserServiceInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.OptionalDouble;

public class UserService implements UserServiceInterface {
    private final UserDAOInterface userDAO;

    public UserService(final UserDAOInterface userDAO) {
        this.userDAO = userDAO;
    }

    // 1. Criar novo usuário
    public void createUser(String login, String password, String avatar) throws SQLException {
        User user = new User(0, login, password, avatar, 0, 0, 0);
        userDAO.create(user);
    }

    // 2. Excluir usuário pelo login
    public boolean deleteUserByLogin(String login) throws SQLException {
        return userDAO.deleteByLogin(login);
    }

    // 3. Listar todos os usuários
    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    // 4. Buscar usuário por login
    public User findUserByLogin(String login) throws SQLException {
        return userDAO.findByLogin(login);
    }

    // 5. Atualizar pontuação/simulações
    public void updateUserSimulationStats(int id, int score, int simulationsRun, int successfulSimulations) throws SQLException {
        userDAO.updateScoreAndSimulations(id, score, simulationsRun, successfulSimulations);
    }

    // ✅ 6. Atualizações Individuais
    public void updateScore(int id, int newScore) throws SQLException {
        userDAO.updateScore(id, newScore);
    }

    public void updateSimulationsRun(int id, int newSimulationsRun) throws SQLException {
        userDAO.updateSimulationsRun(id, newSimulationsRun);
    }

    public void updateSuccessfulSimulations(int id, int newSuccessfulSimulations) throws SQLException {
        userDAO.updateSuccessfulSimulations(id, newSuccessfulSimulations);
    }

    // 7. Obter estatísticas gerais
    public SimulationStats getSimulationStatistics() throws SQLException {
        final var users = userDAO.findAll();
        final var totalSimulations = users.stream().mapToInt(User::simulationsRun).sum();
        final var totalSuccessfulSimulations = users.stream().mapToInt(User::successfulSimulations).sum();
        OptionalDouble avgSuccessPerUser = users.stream()
                .mapToInt(User::successfulSimulations)
                .average();
        final var avgTotalSuccess = !users.isEmpty() ? (double) totalSuccessfulSimulations / users.size() : 0;
        return new SimulationStats(totalSimulations, totalSuccessfulSimulations, avgSuccessPerUser.orElse(0), avgTotalSuccess);
    }

    // Classe interna para representar os dados estatísticos
        public record SimulationStats(int totalSimulations, int totalSuccessfulSimulations, double averageSuccessfulPerUser,
                                      double averageSuccessfulTotal) {

        @Override
            public String toString() {
                return "Estatísticas da Simulação:\n" +
                        "→ Total de Simulações: " + totalSimulations + "\n" +
                        "→ Total de Simulações Bem-sucedidas: " + totalSuccessfulSimulations + "\n" +
                        "→ Média de Simulações Bem-sucedidas por Usuário: " + String.format("%.2f", averageSuccessfulPerUser) + "\n" +
                        "→ Média Geral de Sucesso: " + String.format("%.2f", averageSuccessfulTotal);
            }
        }
}
