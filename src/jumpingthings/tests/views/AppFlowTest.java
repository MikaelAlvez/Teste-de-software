package jumpingthings.tests.views;

import jumpingthings.main.App;
import jumpingthings.main.user.model.User;
import jumpingthings.main.user.protocols.UserServiceInterface;
import jumpingthings.main.views.*;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

// IMPORTANT: This test file should be moved to a proper test source directory (e.g., src/test/java/jumpingthings/test/views)
// and the package declaration should be changed to `package jumpingthings.test.views;`

public class AppFlowTest {

    private FrameFixture window;
    private UserServiceInterface mockUserService;

    @BeforeEach
    public void setUp() throws SQLException, IOException {
        mockUserService = mock(UserServiceInterface.class);

        // Mock user for login tests
        User mockUser = new User(1, "testuser", "password123", "/path/to/avatar.png", 100, 10, 5);
        when(mockUserService.findUserByLogin("testuser")).thenReturn(mockUser);
        when(mockUserService.findUserByLogin("nonexistent")).thenReturn(null);
        when(mockUserService.deleteUserByLogin("testuser")).thenReturn(true);

        // Create a dummy file for avatar selection
        File dummyAvatar = new File("dummy-avatar.png");
        dummyAvatar.createNewFile();
        dummyAvatar.deleteOnExit();

        RouterView frame = GuiActionRunner.execute(() -> {
            RouterView router = RouterView.getInstance();
            router.addView("/", new HomeView());
            router.addView("/sign/in", new SignInView(mockUserService));
            router.addView("/create/user", new CreateUserView(mockUserService));
            router.addView("/delete/user", new DeleteUserView(mockUserService));
            router.addView("/game", new GameView(mockUserService));
            router.addView("/about", new AboutView());
            router.addView("/statistics", new StatisticsView(mockUserService));
            return router;
        });

        window = new FrameFixture(frame);
        window.show(); // Exibe a janela para os testes
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }

    @Test
    public void shouldNavigateToCreateUserAndSave() throws SQLException, IOException {
        // 1. Navega para a tela de criação de usuário
        window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            protected boolean isMatching(JButton button) {
                return "Criar Conta".equals(button.getText());
            }
        }).click();

        // 2. Preenche os campos
        window.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            protected boolean isMatching(JTextField field) {
                return true; // Pega o primeiro JTextField (Login)
            }
        }).enterText("newuser");
        //window.passwordField().enterText("newpass");

        // Mock JFileChooser by finding the button and simulating file selection
        window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            protected boolean isMatching(JButton button) {
                return "Selecionar Arquivo".equals(button.getText());
            }
        }).click();

        JFileChooser fileChooser = window.robot().finder().find(new GenericTypeMatcher<JFileChooser>(JFileChooser.class) {
            @Override
            protected boolean isMatching(JFileChooser component) {
                return true;
            }
        });
        fileChooser.setSelectedFile(new File("dummy-avatar.png"));
        fileChooser.approveSelection();


        // 3. Clica em Salvar
        window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            protected boolean isMatching(JButton button) {
                return "Salvar".equals(button.getText());
            }
        }).click();

        // 4. Verifica se o serviço foi chamado e se a navegação ocorreu
        verify(mockUserService, times(1)).createUser(eq("newuser"), eq("newpass"), anyString());
    }

    @Test
    public void shouldLoginAndNavigateToGame() throws SQLException {
        // 1. Navega para a tela de login
        window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            protected boolean isMatching(JButton button) {
                return "Login".equals(button.getText());
            }
        }).click();

        // 2. Preenche os campos
        window.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            protected boolean isMatching(JTextField field) {
                return true;
            }
        }).enterText("testuser");
        //window.passwordField().enterText("password123");

        // 3. Clica em Login
        window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            protected boolean isMatching(JButton button) {
                return "Login".equals(button.getText());
            }
        }).click();

        // 4. Verifica se o usuário foi autenticado e se a tela do jogo é exibida
        assert "testuser".equals(App.authenticated);
        window.panel(new GenericTypeMatcher<JPanel>(JPanel.class) {
            protected boolean isMatching(JPanel panel) {
                return panel instanceof GameView;
            }
        }).requireVisible();
    }

    @Test
    public void shouldNavigateToAboutView() {
        window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            protected boolean isMatching(JButton button) {
                return "Sobre".equals(button.getText());
            }
        }).click();

        window.panel(new GenericTypeMatcher<JPanel>(JPanel.class) {
            protected boolean isMatching(JPanel panel) {
                return panel instanceof AboutView;
            }
        }).requireVisible();
    }

    @Test
    public void shouldNavigateToDeleteUserAndConfirm() throws SQLException {
        // 1. Navega para a tela de exclusão
        window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            protected boolean isMatching(JButton button) {
                return "Excluir Conta".equals(button.getText());
            }
        }).click();

        // 2. Preenche o login
        window.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            protected boolean isMatching(JTextField field) {
                return true;
            }
        }).enterText("testuser");

        // 3. Clica em Deletar
        window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            protected boolean isMatching(JButton button) {
                return "Deletar".equals(button.getText());
            }
        }).click();

        // 4. Verifica se o serviço foi chamado e se a navegação para a HomeView ocorreu
        verify(mockUserService, times(1)).deleteUserByLogin("testuser");
        window.panel(new GenericTypeMatcher<JPanel>(JPanel.class) {
            protected boolean isMatching(JPanel panel) {
                return panel instanceof HomeView;
            }
        }).requireVisible();
    }
}
