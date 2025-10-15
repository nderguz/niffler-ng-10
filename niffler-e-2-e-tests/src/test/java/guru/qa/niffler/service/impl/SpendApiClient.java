package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.SpendClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CFG.spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);


    @Override
    public SpendJson getSpend(String id, String username) {
        final Response<SpendJson> response;
        try{
            response = spendApi.getSpend(id, username)
                    .execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    public SpendJson getSpends(String username, CurrencyValues filterCurrency, Date from, Date to) {
        final Response<SpendJson> response;
        try{
            response = spendApi.getSpends(username, filterCurrency, from, to)
                    .execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    public SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend)
                    .execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
        return response.body();
    }

    @Override
    public SpendJson editSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try{
            response = spendApi.editSpend(spend)
                    .execute();
        }catch (Exception e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    public void deleteSpend(String username, List<String> ids) {
        final Response<Void> response;
        try{
            response = spendApi.deleteSpends(username, ids)
                    .execute();
        }catch (Exception e){
            throw new AssertionError(e);
        }
        assertEquals(202, response.code());
    }

    @Override
    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try{
            response = spendApi.getCategories(username, excludeArchived)
                    .execute();
        }catch (Exception e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    public CategoryJson addCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try{
            response = spendApi.addCategory(category)
                    .execute();
        }catch (Exception e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try{
            response = spendApi.updateCategory(category)
                    .execute();
        }catch (Exception e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username) {
        throw new UnsupportedOperationException("Not implemented :(");
    }
}
