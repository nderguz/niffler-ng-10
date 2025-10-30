package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.SpendClient;

import java.util.Date;
import java.util.List;


public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();
    private final XaTransactionTemplate txTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

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
        return txTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();
            if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = categoryDaoJdbc.create(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(new SpendDaoJdbc().create(spendEntity));
        });
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
        return txTemplate.execute(() -> {
            var createdCategory = new CategoryDaoJdbc().create(CategoryEntity.fromJson(category));
            return CategoryJson.fromEntity(createdCategory);
        });
    }

    @Override
    public CategoryJson updateCategory(CategoryJson category) {
        return txTemplate.execute(() -> {
            CategoryDao categoryDaoJdbc = new CategoryDaoJdbc();
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            categoryDaoJdbc.update(categoryEntity);
            return category;
        });
    }
}