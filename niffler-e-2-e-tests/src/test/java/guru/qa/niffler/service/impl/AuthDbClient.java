package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaConsumer;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.service.AuthClient;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.xaTransaction;


public class AuthDbClient implements AuthClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public AuthUserJson createWithError(AuthUserJson user) {
        AuthUserEntity ue = new AuthUserEntity();
        ue.setUsername(user.getUsername());
        ue.setPassword(pe.encode(user.getPassword()));
        ue.setEnabled(user.isEnabled());
        ue.setAccountNonExpired(user.isAccountNonExpired());
        ue.setAccountNonLocked(user.isAccountNonLocked());
        ue.setCredentialsNonExpired(user.isCredentialsNonExpired());
        xaTransaction(
                new XaConsumer(
                        connection -> {
                            AuthUserDaoJdbc dao = new AuthUserDaoJdbc(connection);
                            var authenticated = dao.create(ue);
                            ue.setId(authenticated.getId());
                            ue.setPassword(authenticated.getPassword());
                        },
                        CFG.authJdbcUrl()
                ),
                new XaConsumer(
                        connection -> {
                            AuthAuthorityDaoJdbc authorityDao = new AuthAuthorityDaoJdbc(connection);
                            authorityDao.createWithError(Arrays.stream(Authority.values())
                                    .map(a -> {
                                        var ae = new AuthorityEntity();
                                        ae.setAuthority(a);
                                        ae.setUserId(ue.getId());
                                        return ae;
                                    })
                                    .toArray(AuthorityEntity[]::new));
                        },
                        CFG.authJdbcUrl()
                )
        );
        return AuthUserJson.fromEntity(ue);
    }

    @Override
    public AuthUserJson create(AuthUserJson user) {
        AuthUserEntity ue = new AuthUserEntity();
        ue.setUsername(user.getUsername());
        ue.setPassword(pe.encode(user.getPassword()));
        ue.setEnabled(user.isEnabled());
        ue.setAccountNonExpired(user.isAccountNonExpired());
        ue.setAccountNonLocked(user.isAccountNonLocked());
        ue.setCredentialsNonExpired(user.isCredentialsNonExpired());
        xaTransaction(
                new XaConsumer(
                        connection -> {
                            AuthUserDaoJdbc dao = new AuthUserDaoJdbc(connection);
                            var authenticated = dao.create(ue);
                            ue.setId(authenticated.getId());
                            ue.setPassword(authenticated.getPassword());
                        },
                        CFG.authJdbcUrl()
                ),
                new XaConsumer(
                        connection -> {
                            AuthAuthorityDaoJdbc authorityDao = new AuthAuthorityDaoJdbc(connection);
                            authorityDao.create(Arrays.stream(Authority.values())
                                    .map(a -> {
                                        var ae = new AuthorityEntity();
                                        ae.setAuthority(a);
                                        ae.setUserId(ue.getId());
                                        return ae;
                                    }).toArray(AuthorityEntity[]::new));
                        },
                        CFG.authJdbcUrl()
                )
        );
        return AuthUserJson.fromEntity(ue);
    }

    @Override
    public void delete(AuthUserJson user) {
        xaTransaction(
                new XaConsumer(
                        connection -> {
                            AuthAuthorityDaoJdbc authorityDaoJdbc = new AuthAuthorityDaoJdbc(connection);
                            authorityDaoJdbc.delete(authorityDaoJdbc.findAllByUserId(user.getId())
                                    .toArray(AuthorityEntity[]::new));

                        },
                        CFG.authJdbcUrl()
                ),
                new XaConsumer(
                        connection -> {
                            AuthUserDaoJdbc dao = new AuthUserDaoJdbc(connection);
                            dao.deleteById(user.getId());
                        },
                        CFG.authJdbcUrl()
                )
        );
    }
}