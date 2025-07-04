package jumpingthings.tests.user;

import jumpingthings.main.user.dao.UserDAO;
import jumpingthings.main.user.model.User;
import jumpingthings.main.user.service.UserService;
import jumpingthings.main.user.service.UserService.SimulationStats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserDAO mockDAO;
    private UserService service;

    @BeforeEach
    void setUp() {
        mockDAO = mock(UserDAO.class);  // 👉 Dublê de objeto (mock)
        service = new UserService(mockDAO);
    }

    // 1️⃣ Teste de Especificação: Criação de usuário
    @Test
    void testCreateUser_createsUserCorrectly() throws SQLException {
        service.createUser("login1", "senha", "avatar.png");

        // Verifica se o método 'create' do DAO foi chamado
        verify(mockDAO, times(1)).create(any(User.class));
    }

    // 2️⃣ Teste de Fronteira: Excluir usuário com login vazio
    @Test
    void testDeleteUser_emptyLogin_returnsFalse() throws SQLException {
        when(mockDAO.deleteByLogin("")).thenReturn(false);

        boolean result = service.deleteUserByLogin("");
        assertFalse(result);
    }

    // 3️⃣ Teste Estrutural: Verifica todos os caminhos em getSimulationStatistics
    @Test
    void testGetSimulationStatistics_allPaths() throws SQLException {
        List<User> users = Arrays.asList(
                new User(1, "a", "x", "a.png", 10, 5, 3),
                new User(2, "b", "x", "b.png", 20, 15, 10)
        );

        when(mockDAO.findAll()).thenReturn(users);

        SimulationStats stats = service.getSimulationStatistics();

        assertEquals(25, stats.totalSimulations());
        assertEquals(13, stats.totalSuccessfulSimulations());
        assertEquals(6.5, stats.averageSuccessfulPerUser(), 0.01);
        assertEquals(6.5, stats.averageSuccessfulTotal(), 0.01);
    }

    // 4️⃣ Teste Baseado em Propriedade: Total de sucessos não deve exceder total de simulações
    @Test
    void testStatsProperty_successLessThanTotal() throws SQLException {
        List<User> users = List.of(new User(1, "test", "pass", "x", 10, 5, 15));
        when(mockDAO.findAll()).thenReturn(users);

        SimulationStats stats = service.getSimulationStatistics();

        assertTrue(stats.totalSuccessfulSimulations() <= stats.totalSimulations(),
                "Total de simulações bem-sucedidas não pode ser maior que total de simulações.");
    }

    // 5️⃣ Teste de Dublês: Testando interação com o DAO sem acessar o banco real
    @Test
    void testUpdateUserSimulationStats_callsDaoWithCorrectParams() throws SQLException {
        service.updateUserSimulationStats(1, 100, 10, 5);

        verify(mockDAO).updateScoreAndSimulations(1, 100, 10, 5);
    }

    // 6️⃣ Teste de Integração: integração entre Service e DAO (simulado)
    @Test
    void testFindUserByLogin_integrationWithDao() throws SQLException {
        User expected = new User(1, "login", "pass", "avatar", 0, 0, 0);
        when(mockDAO.findByLogin("login")).thenReturn(expected);

        User result = service.findUserByLogin("login");

        assertNotNull(result);
        assertEquals("login", result.login());
    }

    // 7️⃣ Teste de Sistema (simulado): fluxo completo de criação e busca de usuário
    @Test
    void testFullFlow_createAndFindUser() throws SQLException {
        User created = new User(1, "john", "123", "john.png", 0, 0, 0);

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            User u = (User) args[0];
            assertEquals("john", u.login());
            return null;
        }).when(mockDAO).create(any(User.class));

        when(mockDAO.findByLogin("john")).thenReturn(created);

        service.createUser("john", "123", "john.png");
        User found = service.findUserByLogin("john");

        assertEquals("john", found.login());
    }

    // 8️⃣ Teste de Fronteira: Nenhum usuário no sistema
    @Test
    void testSimulationStats_noUsers_returnsZeroStats() throws SQLException {
        when(mockDAO.findAll()).thenReturn(Collections.emptyList());

        SimulationStats stats = service.getSimulationStatistics();

        assertEquals(0, stats.totalSimulations());
        assertEquals(0, stats.totalSuccessfulSimulations());
        assertEquals(0.0, stats.averageSuccessfulPerUser());
        assertEquals(0.0, stats.averageSuccessfulTotal());
    }
}
