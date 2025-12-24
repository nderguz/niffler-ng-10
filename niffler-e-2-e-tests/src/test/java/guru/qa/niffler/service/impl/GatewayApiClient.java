package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.GatewayApi;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.RestClient;
import io.qameta.allure.Step;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public final class GatewayApiClient extends RestClient {

    private final GatewayApi gatewayApi;

    public GatewayApiClient() {
        super(CFG.gatewayUrl(), HttpLoggingInterceptor.Level.NONE);
        this.gatewayApi = create(GatewayApi.class);
    }

    @Step("Получить всех друзей и входящие приглашения из gateway используя эндпоинт /api/friends/all")
    @Nonnull
    public List<UserJson> allFriends(String bearerToken, @Nullable String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = gatewayApi.allFriends(bearerToken, searchQuery).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        if (response.body() != null) {
            return response.body();
        } else {
            return List.of();
        }
    }
}
