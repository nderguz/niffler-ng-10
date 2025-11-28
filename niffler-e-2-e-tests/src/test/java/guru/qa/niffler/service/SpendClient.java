package guru.qa.niffler.service;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SpendClient {
    @Nullable
    SpendJson createSpend(SpendJson spend);

    @Nullable
    CategoryJson createCategory(CategoryJson category);

    @Nullable
    CategoryJson updateCategory(CategoryJson category);
}