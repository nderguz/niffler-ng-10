package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.api.UserdataApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.SpendClient;
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
    public SpendJson createSpend(SpendJson spend) {
        return null;
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return null;
    }

    @Override
    public CategoryJson updateCategory(CategoryJson category) {
        return null;
    }
}
