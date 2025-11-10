package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryJdbc;
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

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
    private final UserdataUserRepository userRepository = new UserdataUserRepositoryJdbc();

    private final XaTransactionTemplate txTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @Override
    public UserJson create(UserJson user) {
        return txTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("123456"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);
                    authUser.setAuthorities(
                            Arrays.stream(Authority.values())
                                    .map(a -> {
                                        var ae = new AuthorityEntity();
                                        ae.setAuthority(a);
                                        ae.setUser(authUser);
                                        return ae;
                                    }).toList()
                    );
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(userRepository.create(UserEntity.fromJson(user)));
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
            userRepository.addInvitation(requesterEntity, addresseeEntity);
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
}