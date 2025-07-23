package jumpingthings.tests.user.sistema;

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

