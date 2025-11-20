package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.UserdataApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.hibernate.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.jupiter.extension.UserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserClient;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class UserApiClient implements UserClient {

    private static final Config CFG = Config.getInstance();

    private final Retrofit userdataRetrofit = new Retrofit.Builder()
            .baseUrl(CFG.userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final UserdataApi userdataApi = userdataRetrofit.create(UserdataApi.class);


    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @Override
    public UserJson create(String username, String password) {
        //todo доделать создание пользователей через API а не через БД

        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            userdataUserRepository.create(userEntity(username)),
                            null
                    );
                }
        );
    }

    @Override
    public List<UserJson> addIncomeInvitation(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final UserJson user = create(randomUsername(), UserExtension.DEFAULT_PASSWORD);
                result.add(user);
                userdataApi.sendInvitation(user.username(), targetUser.username());
            }
        }
        return result;
    }

    @Override
    public List<UserJson> addOutcomeInvitation(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final UserJson user = create(randomUsername(), UserExtension.DEFAULT_PASSWORD);
                result.add(user);
                userdataApi.sendInvitation(targetUser.username(), user.username());
            }
        }
        return List.of();
    }

    @Override
    public List<UserJson> addFriend(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final UserJson user = create(randomUsername(), UserExtension.DEFAULT_PASSWORD);
                result.add(user);
                userdataApi.sendInvitation(targetUser.username(), user.username());
                userdataApi.acceptInvitation(user.username(), targetUser.username());
            }
        }
        return result;
    }

    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }

    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }
}
