package jumpingthings.tests.user.dominio_fronteira;

import jumpingthings.main.user.dao.UserDAO;
import jumpingthings.main.user.model.User;
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

    //Teste de Fronteira: getAllUsers retorna lista vazia
    @Test
    void testGetAllUsers_returnsEmptyList() throws SQLException {
        when(mockDAO.findAll()).thenReturn(Collections.emptyList());

        var users = service.getAllUsers();

        assertNotNull(users);
        assertTrue(users.isEmpty());
        verify(mockDAO).findAll();
    }

    //Teste de Fronteira: getAllUsers retorna lista com um usuário
    @Test
    void testGetAllUsers_returnsSingleUser() throws SQLException {
        var dummyUser = mock(User.class);
        when(mockDAO.findAll()).thenReturn(Collections.singletonList(dummyUser));

        var users = service.getAllUsers();

        assertEquals(1, users.size());
        assertSame(dummyUser, users.get(0));
        verify(mockDAO).findAll();
    }

    //Teste de Fronteira: Atualizar pontuação com valor mínimo
    @Test
    void testUpdateScore_withZeroValue() throws SQLException {
        service.updateScore(1, 0);
        verify(mockDAO).updateScore(1, 0);
    }

    //Teste de Fronteira: Atualizar pontuação com valor máximo
    @Test
    void testUpdateScore_withMaxValue() throws SQLException {
        service.updateScore(1, Integer.MAX_VALUE);
        verify(mockDAO).updateScore(1, Integer.MAX_VALUE);
    }

    //Teste de Fronteira: Atualizar número de simulações com zero
    @Test
    void testUpdateSimulationsRun_withZeroValue() throws SQLException {
        service.updateSimulationsRun(1, 0);
        verify(mockDAO).updateSimulationsRun(1, 0);
    }

    //Teste de Fronteira: Atualizar número de simulações com valor máximo
    @Test
    void testUpdateSimulationsRun_withMaxValue() throws SQLException {
        service.updateSimulationsRun(1, Integer.MAX_VALUE);
        verify(mockDAO).updateSimulationsRun(1, Integer.MAX_VALUE);
    }

    //Teste de Fronteira: Atualizar simulações bem-sucedidas com zero
    @Test
    void testUpdateSuccessfulSimulations_withZeroValue() throws SQLException {
        service.updateSuccessfulSimulations(1, 0);
        verify(mockDAO).updateSuccessfulSimulations(1, 0);
    }

    //Teste de Fronteira: Atualizar simulações bem-sucedidas com valor máximo
    @Test
    void testUpdateSuccessfulSimulations_withMaxValue() throws SQLException {
        service.updateSuccessfulSimulations(1, Integer.MAX_VALUE);
        verify(mockDAO).updateSuccessfulSimulations(1, Integer.MAX_VALUE);
    }

    @Test
    void testSimulationStats_toString_containsExpectedValues() {
        // Criar instância da record com dados arbitrários
        SimulationStats stats = new SimulationStats(10, 5, 2.5, 50.0);

        String result = stats.toString();

        assertNotNull(result);
        assertTrue(result.contains("→ Total de Simulações: 10"));
        assertTrue(result.contains("→ Total de Simulações Bem-sucedidas: 5"));
        assertTrue(result.contains("→ Média de Simulações Bem-sucedidas por Usuário: 2.50"));
        assertTrue(result.contains("→ Média Geral de Sucesso: 50.00"));
    }
}
