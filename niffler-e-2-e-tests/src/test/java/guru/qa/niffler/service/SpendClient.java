package guru.qa.niffler.service;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface SpendClient {
    @Nullable
    SpendJson createSpend(SpendJson spend);

    @Nullable
    CategoryJson createCategory(CategoryJson category);

    @Nullable
    CategoryJson updateCategory(CategoryJson category);

    @Nonnull
    List<CategoryJson> getCategories(String username, boolean excludeArchived);

    @Nonnull
    List<SpendJson> getSpends(String username);
}