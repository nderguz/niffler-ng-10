package guru.qa.niffler.model.user;

public record StaticUser(
        String username,
        String password,
        String friend,
        String income,
        String outcome
) {
}
