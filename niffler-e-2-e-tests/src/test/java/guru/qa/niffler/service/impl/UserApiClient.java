package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.UserdataApi;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.jupiter.extension.UserExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.service.UserClient;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@ParametersAreNonnullByDefault
public final class UserApiClient extends RestClient implements UserClient {

    private final UserdataApi userdataApi;
    private final AuthApi authApi;

    public UserApiClient() {
        super(CFG.userdataUrl());
        this.userdataApi = create(UserdataApi.class);
        this.authApi = create(AuthApi.class);
    }

    @Step("Регистрация нового пользователя через API")
    @Override
    public @Nullable UserJson create(String username, String password) {
        try {
            authApi.requestRegisterForm().execute();
            authApi.register(
                    username,
                    password,
                    password,
                    ThreadSafeCookieStore.INSTANCE.xsrfCookie()
            ).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var createdUser = new UserJson();
        createdUser.setUsername(username);
        createdUser.setCurrency(CurrencyValues.RUB);
        return createdUser;
    }

    @Step("Добавление входящих приглашений дружбы")
    @Override
    public @Nonnull List<UserJson> addIncomeInvitation(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final UserJson user = create(randomUsername(), UserExtension.DEFAULT_PASSWORD);
                result.add(user);
                userdataApi.sendInvitation(user.getUsername(), targetUser.getUsername());
            }
        }
        return result;
    }

    @Step("Добавление исходящих приглашений дружбы")
    @Override
    public @Nonnull List<UserJson> addOutcomeInvitation(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final UserJson user = create(randomUsername(), UserExtension.DEFAULT_PASSWORD);
                result.add(user);
                userdataApi.sendInvitation(targetUser.getUsername(), user.getUsername());
            }
        }
        return result;
    }

    @Step("Добавление в друзья пользователя")
    @Override
    public @Nonnull List<UserJson> addFriend(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final UserJson user = create(randomUsername(), UserExtension.DEFAULT_PASSWORD);
                result.add(user);
                userdataApi.sendInvitation(targetUser.getUsername(), user.getUsername());
                userdataApi.acceptInvitation(user.getUsername(), targetUser.getUsername());
            }
        }
        return result;
    }
}