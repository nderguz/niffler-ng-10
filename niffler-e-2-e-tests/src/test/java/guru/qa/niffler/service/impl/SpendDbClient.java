package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.SpendClient;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
//        return transaction(connection -> {
//            SpendEntity spendEntity = SpendEntity.fromJson(spend);
//            CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc(connection);
//            if (spendEntity.getCategory().getId() == null) {
//                CategoryEntity categoryEntity = categoryDaoJdbc.create(spendEntity.getCategory());
//                spendEntity.setCategory(categoryEntity);
//            }
//            return SpendJson.fromEntity(new SpendDaoJdbc(connection).create(spendEntity));
//        }, CFG.spendJdbcUrl());
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
        return findAllByUsername(username);
    }

    @Override
    public CategoryJson addCategory(CategoryJson category) {
        return CategoryJson.fromEntity(
                new CategoryDaoSpringJdbc(Databases.dataSource(CFG.spendJdbcUrl()))
                        .create(CategoryEntity.fromJson(category))
        );
//        return transaction(connection -> {
//            var createdCategory = new CategoryDaoJdbc(connection).create(CategoryEntity.fromJson(category));
//            return CategoryJson.fromEntity(createdCategory);
//        }, CFG.spendJdbcUrl());
    }

    @Override
    public CategoryJson updateCategory(CategoryJson category) {
        new CategoryDaoSpringJdbc(Databases.dataSource(CFG.spendJdbcUrl()))
                .update(CategoryEntity.fromJson(category));
        return category;

//        return transaction(connection -> {
//            CategoryDao categoryDaoJdbc = new CategoryDaoJdbc(connection);
//            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
//            categoryDaoJdbc.update(categoryEntity);
//            return category;
//        }, CFG.spendJdbcUrl());

    }

    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return Optional.of(
                CategoryJson.fromEntity(
                        new CategoryDaoSpringJdbc(Databases.dataSource(CFG.spendJdbcUrl())).findCategoryById(id).get()
                )
        );
    }

    @Override
    public List<CategoryJson> findAllByUsername(String username) {
        return new CategoryDaoSpringJdbc(Databases.dataSource(CFG.spendJdbcUrl())).findAllByUsername(username)
                .stream()
                .map(CategoryJson::fromEntity)
                .toList();
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        new CategoryDaoSpringJdbc(Databases.dataSource(CFG.spendJdbcUrl()))
                .delete(CategoryEntity.fromJson(category));
    }

    @Override
    public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username) {
        return Optional.of(
                CategoryJson.fromEntity(
                        new CategoryDaoSpringJdbc(Databases.dataSource(CFG.spendJdbcUrl()))
                                .findCategoryByUsernameAndCategoryName(categoryName, username).get()
                )
        );
    }
}