package guru.qa.niffler.test.rest;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.SQLException;

@RestTest
public class ChainedTransactionTest {

    private static final Config CFG = Config.getInstance();

    @Test
    public void successTransactionTest() {
        AuthUserJson user = getNewUserJson();
        AuthorityEntity authority = new AuthorityEntity();
        authority.setAuthority(Authority.read);
        DataSourceTransactionManager authManager = new DataSourceTransactionManager(
                DataSources.dataSource(CFG.authJdbcUrl())
        );
        DataSourceTransactionManager userDataManager = new DataSourceTransactionManager(
                DataSources.dataSource(CFG.userdataJdbcUrl())
        );
        ChainedTransactionManager chainedTxManager = new ChainedTransactionManager(authManager, userDataManager);
        TransactionTemplate txTemplate = new TransactionTemplate(chainedTxManager);

        txTemplate.execute(txStatus -> {
            try (Connection authConnection = authManager.getDataSource().getConnection();
                 Connection userDataConnection = userDataManager.getDataSource().getConnection()) {
                var savedUser = new AuthUserDaoJdbc().create(AuthUserEntity.fromJson(user));
                authority.setUser(savedUser);
                new UserDataDaoJdbc().create(getEntityFromUser(savedUser));
                new AuthAuthorityDaoJdbc().create(authority);
                return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /*
    При попытке создать пустое Authority транзакция не откатилась
     */
    @Test
    @DisplayName("Проваленный тест без отката Authority")
    public void failedTransactionTest() {
        AuthUserJson user = getNewUserJson();

        DataSourceTransactionManager authManager = new DataSourceTransactionManager(
                DataSources.dataSource(CFG.authJdbcUrl())
        );
        DataSourceTransactionManager userDataManager = new DataSourceTransactionManager(
                DataSources.dataSource(CFG.userdataJdbcUrl())
        );
        ChainedTransactionManager chainedTxManager = new ChainedTransactionManager(authManager, userDataManager);
        TransactionTemplate txTemplate = new TransactionTemplate(chainedTxManager);

        txTemplate.execute(txStatus -> {
            try (Connection authConnection = authManager.getDataSource().getConnection();
                 Connection userDataConnection = userDataManager.getDataSource().getConnection()) {
                var savedUser = new AuthUserDaoJdbc().create(AuthUserEntity.fromJson(user));
                new UserDataDaoJdbc().create(getEntityFromUser(savedUser));
                new AuthAuthorityDaoJdbc().create(new AuthorityEntity());
                return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /*
    Здесь транзакция откатилась корректно
     */
    @Test
    @DisplayName("Проваленный тест с откатывающейся транзакцией")
    public void failedTransactionSpringTest() {
        AuthUserJson user = getNewUserJson();

        DataSourceTransactionManager authManager = new DataSourceTransactionManager(
                DataSources.dataSource(CFG.authJdbcUrl())
        );
        DataSourceTransactionManager userDataManager = new DataSourceTransactionManager(
                DataSources.dataSource(CFG.userdataJdbcUrl())
        );
        ChainedTransactionManager chainedTxManager = new ChainedTransactionManager(authManager, userDataManager);
        TransactionTemplate txTemplate = new TransactionTemplate(chainedTxManager);

        txTemplate.execute(txStatus -> {
            try (Connection authConnection = authManager.getDataSource().getConnection();
                 Connection userDataConnection = userDataManager.getDataSource().getConnection()) {
                var savedUser = new AuthUserDaoSpringJdbc().create(AuthUserEntity.fromJson(user));
                new UdUserDaoSpringJdbc().create(getEntityFromUser(savedUser));
                new AuthAuthorityDaoSpringJdbc().create(new AuthorityEntity());
                return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
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
