package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.SpendClient;

import java.util.Date;
import java.util.List;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    @Override
    public SpendJson getSpend(String id, String username) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public SpendJson getSpends(String username, CurrencyValues filterCurrency, Date from, Date to) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc(connection);
            if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = categoryDaoJdbc.create(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(new SpendDaoJdbc(connection).create(spendEntity));
        }, CFG.spendJdbcUrl());
    }

    @Override
    public SpendJson editSpend(SpendJson spend) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deleteSpend(String username, List<String> ids) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public CategoryJson addCategory(CategoryJson category) {
        return transaction(connection -> {
            var createdCategory = new CategoryDaoJdbc(connection).create(CategoryEntity.fromJson(category));
            return CategoryJson.fromEntity(createdCategory);
        }, CFG.spendJdbcUrl());
    }

    @Override
    public CategoryJson updateCategory(CategoryJson category) {
        return transaction(connection -> {
            CategoryDao categoryDaoJdbc = new CategoryDaoJdbc(connection);
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            categoryDaoJdbc.update(categoryEntity);
            return category;
        }, CFG.spendJdbcUrl());
    }
}