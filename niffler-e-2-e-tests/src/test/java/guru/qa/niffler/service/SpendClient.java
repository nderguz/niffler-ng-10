package guru.qa.niffler.service;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendClient {

    SpendJson getSpend(UUID id);

    SpendJson createSpend(SpendJson spend);

    SpendJson editSpend(SpendJson spend);

    CategoryJson createCategory(CategoryJson category);

    CategoryJson updateCategory(CategoryJson category);

    void deleteSpend(SpendJson spend);

    void deleteCategory(CategoryJson category);
}
