package jumpingthings.tests.user.integracao;

import jumpingthings.main.user.dao.UserDAO;
import jumpingthings.main.user.model.User;
import jumpingthings.main.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

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

}
