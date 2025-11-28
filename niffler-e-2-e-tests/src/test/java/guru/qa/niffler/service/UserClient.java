package guru.qa.niffler.service;

import guru.qa.niffler.model.user.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UserClient {
    @Nullable
    UserJson create(String username, String password);

    @Nonnull
    List<UserJson> addIncomeInvitation(UserJson targetUser, int count);

    @Nonnull
    List<UserJson> addOutcomeInvitation(UserJson targetUser, int count);

    @Nonnull
    List<UserJson> addFriend(UserJson targetUser, int count);
}
