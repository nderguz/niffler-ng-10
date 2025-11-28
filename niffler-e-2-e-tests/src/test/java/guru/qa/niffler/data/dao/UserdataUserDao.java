package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.user.UserEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserdataUserDao {
    @Nonnull
    UserEntity create(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    @Nonnull
    UserEntity update(UserEntity user);

    void delete(UserEntity user);

    @Nonnull
    List<UserEntity> findAll();
}
