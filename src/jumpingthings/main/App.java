package jumpingthings.main;

import jumpingthings.main.common.Env;
import jumpingthings.main.user.dao.UserDAO;
import jumpingthings.main.user.service.UserService;
import jumpingthings.main.views.*;

import javax.swing.*;

public class App {
  public static String authenticated;

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      final var userDAO = new UserDAO(Env.DB_URL);
      final var userService = new UserService(userDAO);

      final var router = RouterView.getInstance();
      // Sign In
      router.addView("/sign/in", new SignInView(userService));
      // Create User
      router.addView("/create/user", new CreateUserView(userService));
      // Delete User
      router.addView("/delete/user", new DeleteUserView(userService));
      // Game
      router.addView("/game", new GameView(userService));
      // Statistics
      router.addView("/statistics", new StatisticsView(userService));
      // About
      router.addView("/about", new AboutView());
      // Home
      router.addView("/", new HomeView());
      // Define a view inicial e mostra a janela
      RouterView.setAsMainView("/");
    });
  }
}
