package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.service.SpendClient;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public final class SpendApiClient extends RestClient implements SpendClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        this.spendApi = create(SpendApi.class);
    }

    @Step("Создание траты категории через вызов REST API запроса internal/spends/add")
    @Override
    public @Nullable SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return Objects.requireNonNull(response.body());
    }

    @Step("Создание категории через вызов REST API запроса internal/categories/add")
    @Override
    public @Nullable CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(category).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return Objects.requireNonNull(response.body());
    }

    @Step("Обновление категории через вызов REST API запроса internal/categories/update")
    @Override
    public @Nullable CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return Objects.requireNonNull(response.body());
    }

    @Nonnull
    @Override
    @Step("Получение категорий пользователя через API")
    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        try {
            var response = spendApi.getCategories(username, excludeArchived).execute();
            if(response.body() != null){
                return response.body();
            }else {
                return List.of();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public List<SpendJson> getSpends(String username) {
        try {
            var response = spendApi.getSpends(username, null, null, null).execute();
            if(response.body() != null){
                return response.body();
            }else {
                return List.of();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
