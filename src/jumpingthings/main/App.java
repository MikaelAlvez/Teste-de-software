package jumpingthings.main;

import jumpingthings.main.common.Env;
import jumpingthings.main.user.dao.UserDAO;
import jumpingthings.main.user.service.UserService;
import jumpingthings.main.views.*;

import javax.swing.*;
import java.util.Arrays;

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
            router.addView("/game", new GameView());
            // Statistics
            final var statsList = Arrays.asList(
                    new StatisticsView.UserStats("lucas", 10,8),
                    new StatisticsView.UserStats("pedro", 10,8),
                    new StatisticsView.UserStats("valeria", 10,8)
            );
            router.addView("/statistics", new StatisticsView(statsList));
            // Home
            router.addView("/", new HomeView());
            // Define a view inicial e mostra a janela
            RouterView.setAsMainView("/");
        });
    }
}
