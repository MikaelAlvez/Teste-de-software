package jumpingthings.tests.user.integracao;

import jumpingthings.main.user.dao.UserDAO;
import jumpingthings.main.user.model.User;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOIntegrationTest {

    private static Connection connection;
    private static final String DB_URL = "jdbc:hsqldb:mem:testdb";
    private static UserDAO userDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, "sa", "");
        Statement stmt = connection.createStatement();

        stmt.execute("""
            CREATE TABLE users (
                id INTEGER IDENTITY PRIMARY KEY,
                login VARCHAR(50) NOT NULL,
                password VARCHAR(50) NOT NULL,
                avatar VARCHAR(255),
                score INTEGER DEFAULT 0,
                simulations_run INTEGER DEFAULT 0,
                successful_simulations INTEGER DEFAULT 0
            )
        """);

        stmt.close();
        userDAO = new UserDAO(DB_URL);
    }

    @AfterEach
    void cleanUp() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM users");
        }
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.createStatement().execute("SHUTDOWN");
        connection.close();
    }

    /**
     * Testa se um usuário pode ser criado no banco
     * e recuperado corretamente usando o método findByLogin.
     */
    @Test
    void testCreateAndFindByLogin() throws SQLException {
        User user = new User(0, "john", "1234", "avatar.png", 10, 2, 1);
        userDAO.create(user);

        User retrieved = userDAO.findByLogin("john");

        assertNotNull(retrieved);
        assertEquals("john", retrieved.login());
        assertEquals("1234", retrieved.password());
        assertEquals("avatar.png", retrieved.avatar());
        assertEquals(10, retrieved.score());
        assertEquals(2, retrieved.simulationsRun());
        assertEquals(1, retrieved.successfulSimulations());
    }

    /**
     * Testa se o método findAll retorna todos os usuários cadastrados.
     */
    @Test
    void testFindAll() throws SQLException {
        userDAO.create(new User(0, "user1", "pass1", "a1", 5, 1, 0));
        userDAO.create(new User(0, "user2", "pass2", "a2", 10, 2, 1));

        List<User> users = userDAO.findAll();
        assertEquals(2, users.size());
    }

    /**
     * Testa se um usuário pode ser deletado pelo login,
     * e se a exclusão realmente remove o usuário do banco.
     */
    @Test
    void testDeleteByLogin() throws SQLException {
        userDAO.create(new User(0, "deleteMe", "pass", "avatar", 0, 0, 0));
        boolean deleted = userDAO.deleteByLogin("deleteMe");

        assertTrue(deleted);
        assertNull(userDAO.findByLogin("deleteMe"));
    }

    /**
     * Testa se os campos score, simulations_run e successful_simulations
     * podem ser atualizados corretamente.
     */
    @Test
    void testUpdateScoreAndSimulations() throws SQLException {
        userDAO.create(new User(0, "bob", "pw", "av", 1, 1, 0));
        User user = userDAO.findByLogin("bob");

        userDAO.updateScoreAndSimulations(user.id(), 100, 50, 40);
        User updated = userDAO.findByLogin("bob");

        assertEquals(100, updated.score());
        assertEquals(50, updated.simulationsRun());
        assertEquals(40, updated.successfulSimulations());
    }

    /**
     * Testa se os campos score, simulations_run e successful_simulations
     * podem ser atualizados separadamente com os métodos específicos.
     */
    @Test
    void testUpdateIndividualFields() throws SQLException {
        userDAO.create(new User(0, "alice", "pw", "av", 1, 1, 0));
        User user = userDAO.findByLogin("alice");

        userDAO.updateScore(user.id(), 999);
        userDAO.updateSimulationsRun(user.id(), 88);
        userDAO.updateSuccessfulSimulations(user.id(), 77);

        User updated = userDAO.findByLogin("alice");
        assertEquals(999, updated.score());
        assertEquals(88, updated.simulationsRun());
        assertEquals(77, updated.successfulSimulations());
    }
}
