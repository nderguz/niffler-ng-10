package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.utils.OauthUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class OauthTest {

    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    @User
    public void oauthTest(UserJson user) throws IOException {
        String codeVerifier = OauthUtils.generateCodeVerifier();
        String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);
        authApiClient.authorize(codeChallenge);
        var code = authApiClient.login(user.getUsername(), user.getTestData().password());
        String token = authApiClient.token(code, codeVerifier);
        assertNotNull(token);
        System.out.println("Final Token: " + token);
    }
}
