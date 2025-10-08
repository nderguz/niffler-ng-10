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

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc(categoryDao);

    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(spendDao.create(spendEntity));
    }

    public CategoryJson addCategory(CategoryJson category) {
        var createdCategory = categoryDao.create(CategoryEntity.fromJson(category));
        return CategoryJson.fromEntity(createdCategory);
    }

    public CategoryJson updateCategory(CategoryJson archivedCategory) {
        var category = categoryDao.findCategoryById(archivedCategory.id());
        if(category.isPresent()){
            category.get().setArchived(archivedCategory.archived());
            var updatedCategory = categoryDao.updateCategory(category.get());
            return CategoryJson.fromEntity(updatedCategory.get());
        }
        throw new EntityNotFoundException("Category not found");
    }
}
