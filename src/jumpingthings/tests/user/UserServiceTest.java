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
        mockDAO = mock(UserDAO.class);  // Dublê de objeto (mock)
        service = new UserService(mockDAO);
    }

    //Teste de Especificação: Criação de usuário
    @Test
    void testCreateUser_createsUserCorrectly() throws SQLException {
        service.createUser("login1", "senha", "avatar.png");

        // Verifica se o método 'create' do DAO foi chamado
        verify(mockDAO, times(1)).create(any(User.class));
    }

    //Teste de Especificação: buscar usuário inexistente
    @Test
    void testFindUserByLogin_userNotFound_returnsNull() throws SQLException {
        when(mockDAO.findByLogin("inexistente")).thenReturn(null);

        User result = service.findUserByLogin("inexistente");

        assertNull(result);
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

    //Teste Baseado em Propriedade: Total de sucessos não deve exceder total de simulações
    @Test
    void testStatsProperty_successLessThanTotal() throws SQLException {
        List<User> users = List.of(new User(1, "test", "pass", "x", 10, 15, 10));
        when(mockDAO.findAll()).thenReturn(users);

        SimulationStats stats = service.getSimulationStatistics();

        assertTrue(stats.totalSuccessfulSimulations() <= stats.totalSimulations(),
                "Total de simulações bem-sucedidas não pode ser maior que total de simulações.");
    }

    //Teste Baseado em Propriedade: média por usuário compatível com total
    @Test
    void testStatsProperty_averageTotalMatchesComputation() throws SQLException {
        List<User> users = List.of(
                new User(1, "u1", "p", "a.png", 0, 10, 4),
                new User(2, "u2", "p", "b.png", 0, 5, 2)
        );
        when(mockDAO.findAll()).thenReturn(users);

        SimulationStats stats = service.getSimulationStatistics();
        double expectedAvg = (double) stats.totalSuccessfulSimulations() / users.size();

        assertEquals(expectedAvg, stats.averageSuccessfulTotal(), 0.001);
    }

    //Teste de Dublês: Testando interação com o DAO sem acessar o banco real
    @Test
    void testUpdateUserSimulationStats_callsDaoWithCorrectParams() throws SQLException {
        service.updateUserSimulationStats(1, 100, 10, 5);

        verify(mockDAO).updateScoreAndSimulations(1, 100, 10, 5);
    }

    //Teste com Dublê: criar usuário com dados nulos/vazios
    @Test
    void testCreateUser_withEmptyValues() throws SQLException {
        service.createUser("", "", "");
        verify(mockDAO).create(any(User.class));
    }


    //Teste de Integração: integração entre Service e DAO (simulado)
    @Test
    void testFindUserByLogin_integrationWithDao() throws SQLException {
        User expected = new User(1, "login", "pass", "avatar", 0, 0, 0);
        when(mockDAO.findByLogin("login")).thenReturn(expected);

        User result = service.findUserByLogin("login");

        assertNotNull(result);
        assertEquals("login", result.login());
    }

    //Teste de Integração: deletar e tentar buscar
    @Test
    void testIntegration_deleteThenFindUser() throws SQLException {
        String login = "joao";

        when(mockDAO.deleteByLogin(login)).thenReturn(true);
        when(mockDAO.findByLogin(login)).thenReturn(null);

        boolean deleted = service.deleteUserByLogin(login);
        User result = service.findUserByLogin(login);

        assertTrue(deleted);
        assertNull(result);
    }

    //Teste de Sistema (simulado): fluxo completo de criação e busca de usuário
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

    //Teste de Sistema: fluxo com simulações atualizadas
    @Test
    void testSystem_flowWithSimulationUpdate() throws SQLException {
        User user = new User(1, "maria", "senha", "img.png", 0, 0, 0);
        when(mockDAO.findByLogin("maria")).thenReturn(user);

        service.createUser("maria", "senha", "img.png");
        service.updateUserSimulationStats(1, 50, 10, 8);

        verify(mockDAO).updateScoreAndSimulations(1, 50, 10, 8);
    }
}
