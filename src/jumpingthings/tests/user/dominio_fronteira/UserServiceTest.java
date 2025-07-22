package jumpingthings.tests.user.dominio_fronteira;

import jumpingthings.main.user.dao.UserDAO;
import jumpingthings.main.user.service.UserService;
import jumpingthings.main.user.service.UserService.SimulationStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collections;

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
    //Teste de Fronteira: Excluir usuário com login vazio
    @Test
    void testDeleteUser_emptyLogin_returnsFalse() throws SQLException {
        when(mockDAO.deleteByLogin("")).thenReturn(false);

        boolean result = service.deleteUserByLogin("");
        assertFalse(result);
    }

    //Teste de Fronteira: Nenhum usuário no sistema
    @Test
    void testSimulationStats_noUsers_returnsZeroStats() throws SQLException {
        when(mockDAO.findAll()).thenReturn(Collections.emptyList());

        SimulationStats stats = service.getSimulationStatistics();

        assertEquals(0, stats.totalSimulations());
        assertEquals(0, stats.totalSuccessfulSimulations());
        assertEquals(0.0, stats.averageSuccessfulPerUser());
        assertEquals(0.0, stats.averageSuccessfulTotal());
    }

    //Teste de Fronteira: valores mínimos e máximos em update
    @Test
    void testUpdateUserSimulationStats_withZeroValues() throws SQLException {
        service.updateUserSimulationStats(1, 0, 0, 0);
        verify(mockDAO).updateScoreAndSimulations(1, 0, 0, 0);
    }

    @Test
    void testUpdateUserSimulationStats_withLargeValues() throws SQLException {
        service.updateUserSimulationStats(1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        verify(mockDAO).updateScoreAndSimulations(1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
}
