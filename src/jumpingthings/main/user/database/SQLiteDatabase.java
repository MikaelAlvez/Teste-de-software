package jumpingthings.main.user.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabase {
    public static final String URL = "jdbc:sqlite:jumpthings.db";

    public static Connection getConnection(final String url) throws SQLException {
        return DriverManager.getConnection(url);
    }

    public static void initialize(final String url) {
        try (Connection conn = getConnection(url); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    login TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    avatar TEXT,
                    score INTEGER DEFAULT 0,
                    simulations_run INTEGER DEFAULT 0,
                    successful_simulations INTEGER DEFAULT 0
                );
            """);
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
    }
}
