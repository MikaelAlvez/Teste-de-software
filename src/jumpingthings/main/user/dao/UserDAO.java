package jumpingthings.main.user.dao;

import jumpingthings.main.user.database.SQLiteDatabase;
import jumpingthings.main.user.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final String url;


    public UserDAO(final String url) {
        this.url = url;
    }

    public void create(User user) throws SQLException {
        final String sql = """
            INSERT INTO users (login, password, avatar, score, simulations_run, successful_simulations)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = SQLiteDatabase.getConnection(this.url); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.login());
            stmt.setString(2, user.password());
            stmt.setString(3, user.avatar());
            stmt.setInt(4, user.score());
            stmt.setInt(5, user.simulationsRun());
            stmt.setInt(6, user.successfulSimulations());
            stmt.executeUpdate();
        }
    }

    public boolean deleteByLogin(String login) throws SQLException {
        final String sql = "DELETE FROM users WHERE login = ?";
        try (Connection conn = SQLiteDatabase.getConnection(this.url); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        final String sql = "SELECT * FROM users";
        try (Connection conn = SQLiteDatabase.getConnection(this.url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("avatar"),
                        rs.getInt("score"),
                        rs.getInt("simulations_run"),
                        rs.getInt("successful_simulations")
                ));
            }
        }
        return users;
    }

    public User findByLogin(String login) throws SQLException {
        final String sql = "SELECT * FROM users WHERE login = ?";
        try (Connection conn = SQLiteDatabase.getConnection(this.url); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("login"),
                            rs.getString("password"),
                            rs.getString("avatar"),
                            rs.getInt("score"),
                            rs.getInt("simulations_run"),
                            rs.getInt("successful_simulations")
                    );
                }
            }
        }
        return null;
    }

    public void updateScoreAndSimulations(int id, int score, int simulationsRun, int successfulSimulations) throws SQLException {
        final String sql = """
            UPDATE users
            SET score = ?, simulations_run = ?, successful_simulations = ?
            WHERE id = ?
        """;
        try (Connection conn = SQLiteDatabase.getConnection(this.url); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, score);
            stmt.setInt(2, simulationsRun);
            stmt.setInt(3, successfulSimulations);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }
}
