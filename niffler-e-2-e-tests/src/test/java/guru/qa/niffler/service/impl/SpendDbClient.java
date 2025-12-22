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
import io.qameta.allure.Step;
import org.apache.hc.core5.http.MethodNotSupportedException;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public final class SpendDbClient implements SpendClient {
    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = SpendRepository.getInstance();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    @Step("Создание траты через вызов SQL")
    @Override
    public @Nullable SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = spendRepository.createCategory(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            spendRepository.create(spendEntity)
                    );
                }
        );
    }

    @Step("Создание категории через вызов SQL")
    @Override
    public @Nullable CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> CategoryJson.fromEntity(
                        spendRepository.createCategory(
                                CategoryEntity.fromJson(category)
                        )
                )
        );
    }

    @Step("Обновление категории через вызов SQL")
    @Override
    public @Nullable CategoryJson updateCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> CategoryJson.fromEntity(
                        spendRepository.updateCategory(
                                CategoryEntity.fromJson(category)
                        )
                )
        );
    }

    @NotNull
    @Override
    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        throw new UnsupportedOperationException("Not implemented for DB call");
    }

    @NotNull
    @Override
    public List<SpendJson> getSpends(String username) {
        throw new UnsupportedOperationException("Not implemented for DB call");
    }
}