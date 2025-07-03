package jumpingthings.main.user.protocols;

import jumpingthings.main.user.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAOInterface {
  void create(User user) throws SQLException;

  boolean deleteByLogin(String login) throws SQLException;

  List<User> findAll() throws SQLException;

  User findByLogin(String login) throws SQLException;

  void updateScoreAndSimulations(int id, int score, int simulationsRun, int successfulSimulations) throws SQLException;

  void updateScore(int id, int newScore) throws SQLException;

  void updateSimulationsRun(int id, int newSimulationsRun) throws SQLException;

  void updateSuccessfulSimulations(int id, int newSuccessfulSimulations) throws SQLException;
}
