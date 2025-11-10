package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.hibernate.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.SpendClient;

import java.util.UUID;


public class SpendDbClient implements SpendClient {

    private final SpendRepository spendRepositoryHibernate = new SpendRepositoryHibernate();

    private static final Config CFG = Config.getInstance();
    private final XaTransactionTemplate txTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @Override
    public SpendJson getSpend(UUID id) {
        return SpendJson.fromEntity(spendRepositoryHibernate.findById(id).get());
    }

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return txTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            spendRepositoryHibernate.create(spendEntity);
            spendRepositoryHibernate.createCategory(spendEntity.getCategory());
            return SpendJson.fromEntity(spendEntity);
        });
    }

    @Override
    public SpendJson editSpend(SpendJson spend) {
        return txTemplate.execute(() -> SpendJson.fromEntity(
                spendRepositoryHibernate.update(SpendEntity.fromJson(spend))
        ));
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return txTemplate.execute(() -> CategoryJson.fromEntity(
                spendRepositoryHibernate.createCategory(CategoryEntity.fromJson(category))
        ));
    }

    @Override
    public CategoryJson updateCategory(CategoryJson category) {
        return txTemplate.execute(() -> {
            spendRepositoryHibernate.updateCategory(CategoryEntity.fromJson(category));
            return category;
        });
    }

    @Override
    public void deleteSpend(SpendJson spendJson) {
        txTemplate.execute(() -> {
            spendRepositoryHibernate.remove(SpendEntity.fromJson(spendJson));
            return null;
        });
    }


    @Override
    public void deleteCategory(CategoryJson category) {
        txTemplate.execute(() -> {
            spendRepositoryHibernate.removeCategory(CategoryEntity.fromJson(category));
            return null;
        });
    }
}