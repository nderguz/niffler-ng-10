package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import jakarta.persistence.EntityNotFoundException;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc(connection);
            if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = categoryDaoJdbc.create(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(new SpendDaoJdbc(categoryDaoJdbc, connection).create(spendEntity));
        }, CFG.spendJdbcUrl());
    }

    public CategoryJson addCategory(CategoryJson category) {
        return transaction(connection -> {
            var createdCategory = new CategoryDaoJdbc(connection).create(CategoryEntity.fromJson(category));
            return CategoryJson.fromEntity(createdCategory);
        }, CFG.spendJdbcUrl());
    }

    public CategoryJson updateCategory(CategoryJson archivedCategory) {
        return transaction(connection -> {
            CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc(connection);
            var category = categoryDaoJdbc.findCategoryById(archivedCategory.id());
            if (category.isPresent()) {
                category.get().setArchived(archivedCategory.archived());
                var updatedCategory = categoryDaoJdbc.updateCategory(category.get());
                return CategoryJson.fromEntity(updatedCategory.get());
            }
            throw new EntityNotFoundException("Category not found");
        }, CFG.spendJdbcUrl());

    }
}
