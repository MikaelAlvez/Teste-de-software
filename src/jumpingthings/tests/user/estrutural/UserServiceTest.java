package jumpingthings.tests.user.estrutural;

import jumpingthings.main.user.dao.UserDAO;
import jumpingthings.main.user.model.User;
import jumpingthings.main.user.service.UserService;
import jumpingthings.main.user.service.UserService.SimulationStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserDAO mockDAO;
    private UserService service;

    @BeforeEach
    void setUp() {
        mockDAO = mock(UserDAO.class);  // Dublê de objeto (mock)
        service = new UserService(mockDAO);
    }

    //Teste Estrutural: Verifica todos os caminhos em getSimulationStatistics
    @Test
    void testGetSimulationStatistics_allPaths() throws SQLException {
        List<User> users = Arrays.asList(
                new User(1, "a", "x", "a.png", 10, 5, 3),
                new User(2, "b", "x", "b.png", 20, 15, 10)
        );

        when(mockDAO.findAll()).thenReturn(users);

        SimulationStats stats = service.getSimulationStatistics();

        assertEquals(20, stats.totalSimulations());
        assertEquals(13, stats.totalSuccessfulSimulations());
        assertEquals(6.5, stats.averageSuccessfulPerUser(), 0.01);
        assertEquals(6.5, stats.averageSuccessfulTotal(), 0.01);
    }

    //Teste Estrutural: múltiplos usuários com zero simulações
    @Test
    void testGetSimulationStatistics_usersWithZeroSimulations() throws SQLException {
        List<User> users = Arrays.asList(
                new User(1, "a", "x", "a.png", 0, 0, 0),
                new User(2, "b", "y", "b.png", 0, 0, 0)
        );

        when(mockDAO.findAll()).thenReturn(users);

        SimulationStats stats = service.getSimulationStatistics();

        assertEquals(0, stats.totalSimulations());
        assertEquals(0, stats.totalSuccessfulSimulations());
        assertEquals(0.0, stats.averageSuccessfulPerUser(), 0.01);
        assertEquals(0.0, stats.averageSuccessfulTotal(), 0.01);
    }
}

