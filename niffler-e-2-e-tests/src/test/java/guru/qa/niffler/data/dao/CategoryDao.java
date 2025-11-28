package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface CategoryDao {
    @Nonnull
    CategoryEntity create(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName);

    @Nonnull
    List<CategoryEntity> findAllByUsername(String username);

    @Nonnull
    List<CategoryEntity> findAll();

    void delete(CategoryEntity category);

    @Nonnull
    CategoryEntity update(CategoryEntity category);
}
