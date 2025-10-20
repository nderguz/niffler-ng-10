package guru.qa.niffler.test.rest;

import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.service.impl.UserDbClient;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class AuthTest {

    @Test
    public void successTransactionTest() {
        UserDbClient dbClient = new UserDbClient();
        AuthUserJson user = new AuthUserJson();
        user.setUsername(randomUsername());
        user.setPassword(randomPassword());
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        dbClient.create(user);
    }
}