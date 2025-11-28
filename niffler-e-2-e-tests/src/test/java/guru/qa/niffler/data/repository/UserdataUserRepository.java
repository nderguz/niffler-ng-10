package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.repository.impl.hibernate.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.jdbc.SpendRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.jdbc.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.springjdbc.SpendRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.springjdbc.UserdataUserRepositorySpringJdbc;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserdataUserRepository {

    @Nonnull
    static UserdataUserRepository getInstance() {
        return switch (System.getProperty("repository", "jpa")) {
            case "jpa" -> new UserdataUserRepositoryHibernate();
            case "jdbc" -> new UserdataUserRepositoryJdbc();
            case "spring" -> new UserdataUserRepositorySpringJdbc();
            default -> throw new IllegalStateException("Unknown repository: " + System.getProperty("repository"));
        };
    }

    @Nonnull
    UserEntity create(UserEntity user);

    @Nonnull
    UserEntity update(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    void addFriendshipRequest(UserEntity requester, UserEntity addressee);

    void addFriend(UserEntity requester, UserEntity addressee);
}