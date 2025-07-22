package jumpingthings.tests.user.baseado_em_propriedade;

import jumpingthings.main.user.model.User;
import jumpingthings.main.user.protocols.UserDAOInterface;
import jumpingthings.main.user.service.UserService;
import net.jqwik.api.*;
import net.jqwik.api.lifecycle.BeforeTry;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceTest {

    UserDAOInterface userDAO;
    UserService userService;

    @BeforeTry
    void setUp() {
        userDAO = Mockito.mock(UserDAOInterface.class);
        userService = new UserService(userDAO);
    }

    /** Verifica se o método createUser da classe UserService está corretamente repassando os dados recebidos (login, senha, avatar) para o DAO. */
    @Property
    void createUserShouldPassValidUserToDAO(
            @ForAll("validLogins") String login,
            @ForAll("validPasswords") String password,
            @ForAll("validAvatars") String avatar
    ) throws SQLException {
        userService.createUser(login, password, avatar);

        Mockito.verify(userDAO).create(Mockito.argThat(user ->
                user.login().equals(login) &&
                        user.password().equals(password) &&
                        user.avatar().equals(avatar)
        ));
    }

    /** Garante que ao chamar deleteUserByLogin, o método correspondente no DAO (deleteByLogin) seja chamado corretamente com o login fornecido */
    @Property
    void deleteUserShouldCallDAO(
            @ForAll("validLogins") String login
    ) throws SQLException {
        Mockito.when(userDAO.deleteByLogin(login)).thenReturn(true);

        boolean result = userService.deleteUserByLogin(login);

        Mockito.verify(userDAO).deleteByLogin(login);
        assertThat(result).isTrue();
    }

    @Property
    void getSimulationStatisticsShouldNotFail(
            @ForAll("usersWithStats") List<User> users
    ) throws SQLException {
        Mockito.when(userDAO.findAll()).thenReturn(users);

        UserService.SimulationStats stats = userService.getSimulationStatistics();

        int totalSim = users.stream().mapToInt(User::simulationsRun).sum();
        int totalSuccess = users.stream().mapToInt(User::successfulSimulations).sum();

        assertThat(stats.totalSimulations()).isEqualTo(totalSim);
        assertThat(stats.totalSuccessfulSimulations()).isEqualTo(totalSuccess);
        assertThat(stats.averageSuccessfulTotal()).isBetween(0.0, Double.MAX_VALUE);
    }

    // Estratégias de geração
    @Provide
    Arbitrary<String> validLogins() {
        return Arbitraries.strings().withCharRange('a', 'z')
                .ofMinLength(3).ofMaxLength(15);
    }

    @Provide
    Arbitrary<String> validPasswords() {
        return Arbitraries.strings().withCharRange('a', 'z')
                .ofMinLength(6).ofMaxLength(20);
    }

    @Provide
    Arbitrary<String> validAvatars() {
        return Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(10);
    }

    @Provide
    Arbitrary<List<User>> usersWithStats() {
        return Arbitraries.integers().between(0, 100).list().ofMinSize(1).ofMaxSize(20).map(scores -> {
            return scores.stream().map(score -> new User(
                    1,
                    "login" + score,
                    "pass",
                    "avatar",
                    score * 10,
                    score,
                    score / 2
            )).toList();
        });
    }
}
