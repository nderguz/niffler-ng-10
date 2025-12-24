package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.CodeInterceptor;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.utils.OauthUtils;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public final class AuthApiClient extends RestClient {

    private static final String RESPONSE_TYPE = "code";
    private static final String CLIENT_ID = "client";
    private static final String SCOPE = "openid";
    private static final String CODE_CHALLENGE_METHOD = "S256";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String REDIRECT_URL = CFG.frontUrl() + "authorized";

    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl(), true, HttpLoggingInterceptor.Level.NONE, new CodeInterceptor());
        this.authApi = create(AuthApi.class);
    }

    @Step("Вызов REST запросов для регистрации пользователя")
    public Response<Void> register(String username, String password) throws IOException {
        authApi.requestRegisterForm().execute();
        return authApi.register(
                username,
                password,
                password,
                ThreadSafeCookieStore.INSTANCE.xsrfCookie()
        ).execute();
    }

    @SneakyThrows
    public void authorize(String codeChallenge) {
        authApi.authorize(
                RESPONSE_TYPE,
                CLIENT_ID,
                SCOPE,
                REDIRECT_URL,
                codeChallenge,
                CODE_CHALLENGE_METHOD
        ).execute();
    }

    @SneakyThrows
    public String token(String codeVerifier){
        var response = authApi.token(
                ApiLoginExtension.getCode(),
                REDIRECT_URL,
                CLIENT_ID,
                codeVerifier,
                GRANT_TYPE
        ).execute();
        return response.body().get("id_token").asText();
    }

    @SneakyThrows
    public void login(String username, String password){
        authApi.login(username,
                        password,
                        ThreadSafeCookieStore.INSTANCE.xsrfCookie())
                .execute();
    }

    @SneakyThrows
    public String loginUser(String username, String password) {
        final String codeVerifier = OauthUtils.generateCodeVerifier();
        final String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);

        authorize(codeChallenge);
        login(username, password);
        return token(codeVerifier);
    }
}
