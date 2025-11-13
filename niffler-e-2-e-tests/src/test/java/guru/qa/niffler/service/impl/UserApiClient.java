package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.UserdataApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class UserApiClient implements UserClient {

    private static final Config CFG = Config.getInstance();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CFG.userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final UserdataApi userdataApi = retrofit.create(UserdataApi.class);

    @Override
    public UserJson create(String username, String password) {
        throw new UnsupportedOperationException("Not implemented in UserApi");
    }

    @Override
    public List<UserJson> addIncomeInvitation(UserJson targetUser, int count) {
        return List.of();
    }

    @Override
    public List<UserJson> addOutcomeInvitation(UserJson targetUser, int count) {
        return List.of();
    }

    @Override
    public List<UserJson> addFriend(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {

        }
        return List.of();
    }
}
