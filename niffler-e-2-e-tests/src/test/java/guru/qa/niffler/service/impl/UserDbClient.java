package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.service.UserClient;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;


public class UserDbClient implements UserClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
    private final UserdataUserDao userdataUserDao = new UdUserDaoSpringJdbc();


    private final XaTransactionTemplate txTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @Override
    public UserJson create(AuthUserJson user) {
        return txTemplate.execute(() -> {

                    user.setPassword(pe.encode(user.getPassword()));
                    AuthUserEntity ue = AuthUserEntity.fromJson(user);

                    AuthUserEntity createdUser = authUserDao.create(ue);
                    var authEntities = Arrays.stream(Authority.values())
                            .map(a -> {
                                var ae = new AuthorityEntity();
                                ae.setAuthority(a);
                                ae.setUserId(ue.getId());
                                return ae;
                            }).toArray(AuthorityEntity[]::new);
                    authAuthorityDao.create(authEntities);
                    return new UserJson();
                }
        );
    }
}