package jumpingthings.tests.user.duble;

import jumpingthings.main.user.dao.UserDAO;
import jumpingthings.main.user.model.User;
import jumpingthings.main.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

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
}

