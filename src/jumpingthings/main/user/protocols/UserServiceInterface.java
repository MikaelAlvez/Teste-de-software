package jumpingthings.main.user.protocols;

import jumpingthings.main.user.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserServiceInterface {
  void createUser(String login, String password, String avatar) throws SQLException;

  boolean deleteUserByLogin(String login) throws SQLException;

  List<User> getAllUsers() throws SQLException;

  User findUserByLogin(String login) throws SQLException;

  void updateUserSimulationStats(int id, int score, int simulationsRun, int successfulSimulations) throws SQLException;

  void updateScore(int id, int newScore) throws SQLException;

  void updateSimulationsRun(int id, int newSimulationsRun) throws SQLException;

  void updateSuccessfulSimulations(int id, int newSuccessfulSimulations) throws SQLException;

}