package jumpingthings.main.user.model;

public record User(int id, String login, String password, String avatar, int score, int simulationsRun, int successfulSimulations) {
}
