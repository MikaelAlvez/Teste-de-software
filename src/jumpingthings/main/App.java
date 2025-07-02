package jumpingthings.main;

import jumpingthings.main.views.*;

import javax.swing.*;
import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final var router = RouterView.getInstance();
            // Sign In
            router.addView("/sign/in", new SignInView());
            // Create User
            router.addView("/create/user", new CreateUserView());
            // Delete User
            router.addView("/delete/user", new DeleteUserView());
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
