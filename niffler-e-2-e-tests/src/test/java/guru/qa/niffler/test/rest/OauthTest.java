package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.model.user.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@RestTest
public class OauthTest {

    @Test
    @User
    @ApiLogin
    public void oauthTest(@Token String token, UserJson user) {
        System.out.println(user);
        Assertions.assertNotNull(token);
    }
}
