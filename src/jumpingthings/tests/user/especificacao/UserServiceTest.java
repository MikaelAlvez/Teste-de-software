package jumpingthings.tests.user.especificacao;

import jumpingthings.main.user.dao.UserDAO;
import jumpingthings.main.user.model.User;
import jumpingthings.main.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
}

