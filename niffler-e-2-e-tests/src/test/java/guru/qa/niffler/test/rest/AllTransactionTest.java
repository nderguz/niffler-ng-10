package guru.qa.niffler.test.rest;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UserDataDaoJdbc;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class AllTransactionTest {

    private static final Config CFG = Config.getInstance();

    @Test
    public void jdbcWithTransactionTest() {
        AuthUserJson user = getNewUserJson();
        XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
                CFG.authJdbcUrl(),
                CFG.userdataJdbcUrl()
        );
        AuthUserDao authUserDao = new AuthUserDaoJdbc();
        UserdataUserDao userdataUserDao = new UserDataDaoJdbc();
        xaTransactionTemplate.execute(() -> {
            var savedUser = authUserDao.create(AuthUserEntity.fromJson(user));
            userdataUserDao.create(getEntityFromUser(savedUser));
            return null;
        });
    }

    @Test
    public void jdbcWithoutTransactionTest() {
        AuthUserJson user = getNewUserJson();
        try (Connection authConnection = DataSources.dataSource(CFG.authJdbcUrl()).getConnection();
             Connection userdataConnection = DataSources.dataSource(CFG.userdataJdbcUrl()).getConnection()) {
            var savedUser = new AuthUserDaoJdbc().create(AuthUserEntity.fromJson(user));
            new UserDataDaoJdbc().create(getEntityFromUser(savedUser));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void springJdbcWithTransactionTest() {
        AuthUserJson user = getNewUserJson();

        XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
                CFG.authJdbcUrl(),
                CFG.userdataJdbcUrl()
        );
        AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
        UserdataUserDao userdataUserDao = new UdUserDaoSpringJdbc();

        xaTransactionTemplate.execute(() -> {
            var savedUser = authUserDao.create(AuthUserEntity.fromJson(user));
            userdataUserDao.create(getEntityFromUser(savedUser));
            return null;
        });
    }

    @Test
    public void springJdbcWithoutTransactionTest() {
        AuthUserJson user = getNewUserJson();
        AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
        UserdataUserDao userdataUserDao = new UdUserDaoSpringJdbc();

        var savedUser = authUserDao.create(AuthUserEntity.fromJson(user));
        userdataUserDao.create(getEntityFromUser(savedUser));
    }

    private AuthUserJson getNewUserJson() {
        AuthUserJson newUser = new AuthUserJson();
        newUser.setUsername(RandomDataUtils.randomUsername());
        newUser.setPassword(RandomDataUtils.randomPassword());
        newUser.setEnabled(true);
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);
        return newUser;
    }

    private UserEntity getEntityFromUser(AuthUserEntity authEntity) {
        UserEntity ue = new UserEntity();
        ue.setUsername(authEntity.getUsername());
        ue.setFullname(RandomDataUtils.randomName());
        ue.setFirstname(RandomDataUtils.randomName());
        ue.setSurname(RandomDataUtils.randomName());
        ue.setCurrency(CurrencyValues.RUB);
        ue.setPhoto(new byte[0]);
        ue.setPhotoSmall(new byte[0]);
        return ue;
    }
}
