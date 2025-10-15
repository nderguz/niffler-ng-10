package guru.qa.niffler.service;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendClient {

    SpendJson getSpend(String id, String username);

    SpendJson getSpends(String username, CurrencyValues filterCurrency, Date from, Date to);

    SpendJson createSpend(SpendJson spend);

    SpendJson editSpend(SpendJson spend);

    void deleteSpend(String username, List<String> ids);

    List<CategoryJson> getCategories(String username, boolean excludeArchived);

    CategoryJson addCategory(CategoryJson category);

    CategoryJson updateCategory(CategoryJson category);

    Optional<CategoryJson> findCategoryById(UUID id);

    List<CategoryJson> findAllByUsername(String username);

    void deleteCategory(CategoryJson category);

    Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username);
}
