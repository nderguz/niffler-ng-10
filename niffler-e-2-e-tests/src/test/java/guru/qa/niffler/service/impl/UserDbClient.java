package guru.qa.niffler.service.impl;

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
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserClient;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Objects;


public class UserDbClient implements UserClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository userRepository = new UserdataUserRepositoryHibernate();

    private final XaTransactionTemplate txTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @Override
    public UserJson create(String username, String password) {
        return txTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            userRepository.create(userEntity(username)));
                }
        );
    }

    @Override
    public void addInvitation(UserJson requester, UserJson addressee) {
        if (Objects.equals(requester, addressee)) {
            throw new RuntimeException("Can`t create friendship request for self user");
        }
        txTemplate.execute(() -> {
            var requesterEntity = userRepository.findById(requester.id())
                    .orElseThrow(() -> new RuntimeException("User with id = %s not found".formatted(requester.id())));
            var addresseeEntity = userRepository.findById(addressee.id())
                    .orElseThrow(() -> new RuntimeException("User with id = %s not found".formatted(requester.id())));
            userRepository.sendInvitation(requesterEntity, addresseeEntity);
            return null;
        });
    }

    @Override
    public void addFriend(UserJson requester, UserJson addressee) {
        if (Objects.equals(requester, addressee)) {
            throw new RuntimeException("Can`t create friendship request for self user");
        }
        txTemplate.execute(() -> {
            var requesterEntity = userRepository.findById(requester.id())
                    .orElseThrow(() -> new RuntimeException("User with id = %s not found".formatted(requester.id())));
            var addresseeEntity = userRepository.findById(addressee.id())
                    .orElseThrow(() -> new RuntimeException("User with id = %s not found".formatted(requester.id())));
            userRepository.addFriend(requesterEntity, addresseeEntity);
            return null;
        });
    }

    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity ue = new AuthUserEntity();
        ue.setUsername(username);
        ue.setPassword(pe.encode(password));
        ue.setEnabled(true);
        ue.setAccountNonExpired(true);
        ue.setAccountNonLocked(true);
        ue.setCredentialsNonExpired(true);
        ue.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(ue);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return ue;
    }

    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }
}