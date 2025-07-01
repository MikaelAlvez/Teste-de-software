package jumpingthings.main.user.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabase {
    private static final String URL = "jdbc:sqlite:jumpthings.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(SQLiteDatabase.URL);
    }

    public static void initialize() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
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
