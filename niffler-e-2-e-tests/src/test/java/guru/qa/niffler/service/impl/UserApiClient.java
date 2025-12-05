package guru.qa.niffler.service.impl;

import com.codeborne.selenide.Stopwatch;
import guru.qa.niffler.api.UserdataApi;
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

    private static final Long MAX_TIMEOUT_RESPONSE_TIME = 5000L;
    private final UserdataApi userdataApi;
    private final AuthApiClient authApiClient = new AuthApiClient();

    public UserApiClient() {
        super(CFG.userdataUrl());
        this.userdataApi = create(UserdataApi.class);
    }

    @Step("Регистрация нового пользователя через API")
    @Override
    public @Nonnull UserJson create(String username, String password) {
        UserJson user = null;
        try {
            authApiClient.register(username, password);
            Stopwatch sw = new Stopwatch(MAX_TIMEOUT_RESPONSE_TIME);
            while (!sw.isTimeoutReached()) {
                user = userdataApi.currentUser(username).execute().body();
                if (user != null && user.getId() != null) {
                    return user;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (user == null) {
            throw new RuntimeException("Error while creating user");
        }
        return user;
    }

    @Step("Добавление входящих приглашений дружбы")
    @Override
    public @Nonnull List<UserJson> addIncomeInvitation(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final UserJson user = create(randomUsername(), UserExtension.DEFAULT_PASSWORD);
                result.add(user);
                try {
                    userdataApi.sendInvitation(user.getUsername(), targetUser.getUsername()).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
                try {
                    userdataApi.sendInvitation(targetUser.getUsername(), user.getUsername()).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
                try {
                    var udResult = userdataApi.sendInvitation(targetUser.getUsername(), user.getUsername()).execute();
                    Stopwatch sw = new Stopwatch(MAX_TIMEOUT_RESPONSE_TIME);
                    while(!sw.isTimeoutReached() && !udResult.isSuccessful()){
                        Thread.sleep(100);
                    }
                    var acceptResult = userdataApi.acceptInvitation(user.getUsername(), targetUser.getUsername()).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
        }
        return result;
    }

    @Nonnull
    @Override
    public List<UserJson> allUsers(String username, @Nullable String searchQuery) {
        try {
            var result = userdataApi.allUsers(username, searchQuery).execute();
            if (result.body() != null) {
                return result.body();
            } else {
                return List.of();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}