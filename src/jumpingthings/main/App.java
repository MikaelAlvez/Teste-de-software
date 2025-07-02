package jumpingthings.main;

import jumpingthings.main.views.CreateUserView;
import jumpingthings.main.views.DeleteUserView;
import jumpingthings.main.views.RouterView;
import jumpingthings.main.views.SignInView;

import javax.swing.*;

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
            // Define a view inicial e mostra a janela
            RouterView.setAsMainView("/sign/in");
        });
    }
}
