package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.GatewayV2Api;
import guru.qa.niffler.model.page.RestResponsePage;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.RestClient;
import io.qameta.allure.Step;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.data.domain.PageImpl;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public final class GatewayV2ApiClient extends RestClient {

    private final GatewayV2Api gatewayApi;

    public GatewayV2ApiClient() {
        super(CFG.gatewayUrl(), HttpLoggingInterceptor.Level.NONE);
        this.gatewayApi = create(GatewayV2Api.class);
    }

    @Step("Получить всех друзей и входящие приглашения из gateway используя эндпоинт /api/v2/friends/all")
    @Nonnull
    public RestResponsePage<UserJson> allFriends(String bearerToken,
                                                 int page,
                                                 int size,
                                                 List<String> sort,
                                                 @Nullable String searchQuery) {
        final Response<RestResponsePage<UserJson>> response;
        try {
            response = gatewayApi.allFriends(bearerToken, page, size, sort, searchQuery).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        if (response.body() != null) {
            return response.body();
        } else {
            throw new RuntimeException("Empty PageImpl response");
        }
    }
}
