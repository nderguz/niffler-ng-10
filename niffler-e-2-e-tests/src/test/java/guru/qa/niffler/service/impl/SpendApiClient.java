package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.SpendClient;
import lombok.SneakyThrows;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class SpendApiClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CFG.spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Override
    @SneakyThrows
    public SpendJson createSpend(SpendJson spend) {
        var response = spendApi.addSpend(spend).execute();
        return response.body();
    }

    @Override
    @SneakyThrows
    public CategoryJson createCategory(CategoryJson category) {
        var response = spendApi.addCategory(category).execute();
        return response.body();
    }

    @Override
    @SneakyThrows
    public CategoryJson updateCategory(CategoryJson category) {
        var response = spendApi.updateCategory(category).execute();
        return response.body();
    }
}
