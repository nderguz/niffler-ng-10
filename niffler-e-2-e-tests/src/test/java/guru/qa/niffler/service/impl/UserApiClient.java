package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.UserdataApi;
import guru.qa.niffler.jupiter.extension.UserExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.service.UserClient;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;

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
            user = userdataApi.currentUser(username).execute().body();
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

    @NotNull
    @Override
    public List<UserJson> allUsers(String username, @Nullable String searchQuery) {
        try {
            var result = userdataApi.allUsers(username, searchQuery).execute();
            if(result.body() != null){
                return result.body();
            }else{
                return List.of();
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}