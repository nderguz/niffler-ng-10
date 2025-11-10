package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

public class UserdataUserRepositoryHibernate implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();
    private final EntityManager manager = em(CFG.userdataJdbcUrl());

    @Override
    public UserEntity create(UserEntity user) {
        manager.joinTransaction();
        manager.persist(user);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return Optional.ofNullable(manager.find(UserEntity.class, id));
    }

    @Override
    public void addInvitation(UserEntity requester, UserEntity addressee) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
