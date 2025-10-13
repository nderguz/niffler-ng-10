package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;
import java.util.UUID;

public interface AuthAuthorityDao {

    List<AuthorityEntity> create(AuthorityEntity... authorities);

    List<AuthorityEntity> findAllByUserId(UUID userId);

    List<AuthorityEntity> createWithError(AuthorityEntity... authorities);

    void delete(AuthorityEntity... authority);
}
