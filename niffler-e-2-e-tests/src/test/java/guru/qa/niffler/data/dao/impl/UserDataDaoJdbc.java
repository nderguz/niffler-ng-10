package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

public class UserDataDaoJdbc implements UserdataUserDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity user) {
        return null;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void delete(UserEntity user) {

    }
}
