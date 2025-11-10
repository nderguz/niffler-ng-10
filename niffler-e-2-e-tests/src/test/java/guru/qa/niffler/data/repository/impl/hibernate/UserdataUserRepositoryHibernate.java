package guru.qa.niffler.data.repository.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

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
        return Optional.ofNullable(
                manager.find(UserEntity.class, id)
        );
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try {
            return Optional.ofNullable(
                    manager.createQuery(
                                    "SELECT u FROM UserEntity u WHERE u.username =: username", UserEntity.class
                            ).setParameter("username", username)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public UserEntity update(UserEntity user) {
        manager.joinTransaction();
        manager.merge(user);
        return user;
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        manager.joinTransaction();
        requester.addFriends(FriendshipStatus.PENDING, addressee);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        manager.joinTransaction();
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
    }

    @Override
    public void remove(UserEntity user) {
        manager.joinTransaction();
        manager.remove(user);
    }
}
