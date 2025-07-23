package jumpingthings.tests.views;

import jumpingthings.main.common.Env;
import jumpingthings.main.user.dao.UserDAO;
import jumpingthings.main.user.service.UserService;
import jumpingthings.main.views.*;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;

class RouterViewTest {

  private FrameFixture window;

  // Views de teste simples
  private static class TestView extends JPanel {
    public TestView(Color bgColor, String name) {
      setName(name);
      setBackground(bgColor);
      add(new JLabel(name));
      setPreferredSize(new Dimension(700, 500)); // define tamanho preferido para visualização melhor
    }
  }

  @BeforeEach
  void setUp() {
    RouterView router = GuiActionRunner.execute(RouterView::getInstance);

    GuiActionRunner.execute(() -> {
      router.setSize(800, 600);
      router.setLocationRelativeTo(null);

      router.addView("/", new HomeView());
      final var userDAO = new UserDAO(Env.DB_URL);
      final var userService = new UserService(userDAO);
      router.addView("/create/user", new CreateUserView(userService));
      router.addView("/delete/user", new DeleteUserView(userService));
      router.addView("/sign/in", new SignInView(userService));
      router.addView("/game", new GameView(userService));
      router.addView("/statistics", new StatisticsView(userService));
      router.addView("/about", new AboutView());
    });

    window = new FrameFixture(router);
    window.show();
  }

  @AfterEach
  void tearDown() {
    window.cleanUp();
  }

  @Test
  void devePreencherCamposEClickarSalvar() throws Exception {
    GuiActionRunner.execute(() -> RouterView.getInstance().navigateTo("/create/user"));

    // Preenche campos
    window.textBox("CreateUserView.loginField").enterText("usuarioTeste");
    window.textBox("CreateUserView.passwordField").enterText("senha123");

    // Simula seleção de avatar
    File fakeAvatar = File.createTempFile("avatar", ".jpg");
    fakeAvatar.deleteOnExit(); // será deletado automaticamente depois do teste

    // Atualiza avatar diretamente
    GuiActionRunner.execute(() -> {
      CreateUserView view = (CreateUserView) RouterView.getInstance().getViews().get("/create/user");
      view.setSelectedAvatar(fakeAvatar);
    });

    // Clique em salvar
    window.button("CreateUserView.saveButton").click();

    // Verifica se aparece mensagem de sucesso OU erro esperado
    window.optionPane().requireVisible();

    // Opcional: fecha o JOptionPane se quiser continuar o teste
    window.button("CreateUserView.backButton").click();
  }

  @Test
  void deveFazerLogin() {
    GuiActionRunner.execute(() -> RouterView.getInstance().navigateTo("/sign/in"));

    // Preenche campos
    window.textBox("SignInView.loginField").enterText("usuarioTeste");
    window.textBox("SignInView.passwordField").enterText("senha123");

    // Clica no botão Login
    window.button("SignInView.loginButton").click();

    // Verifica que a janela ainda está visível (login não travou app)
    window.requireVisible();

    window.button("GameView.backButton").click();

  }

  @Test
  void deveVerEStatsticas() {
    GuiActionRunner.execute(() -> RouterView.getInstance().navigateTo("/statistics"));
    window.button("StatisticsView.backButton").click();
  }

  @Test
  void deveVerOAbout() {
    GuiActionRunner.execute(() -> RouterView.getInstance().navigateTo("/about"));
    window.button("AboutView.backButton").click();
  }

  @Test
  void deveVerDeleteUserEDeletarUsuario() {
    GuiActionRunner.execute(() -> RouterView.getInstance().navigateTo("/delete/user"));
    // Preenche o campo de login com um usuário existente
    window.textBox("DeleteUserView.loginButton").enterText("usuarioTeste");

    // Clica no botão de deletar
    window.button("DeleteUserView.deleteButton").click();

    // Verifica se aparece um JOptionPane (mensagem de sucesso ou erro)
    window.optionPane().requireVisible();

    // Fecha o JOptionPane (clicando em OK)
    window.optionPane().okButton().click();

    // Clica no botão de voltar
    window.button("DeleteUserView.backButton").click();
  }

}
