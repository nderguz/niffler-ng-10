package guru.qa.niffler.test.graphql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.StatQuery;
import guru.qa.niffler.jupiter.annotation.*;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class StatGraphQlTest extends BaseGraphQlTest {

    @Test
    @User(
            categories = {
                    @Category(name = "Active category 1"),
                    @Category(name = "Active category 2"),
                    @Category(name = "Archive category 1", archived = true),
                    @Category(name = "Archive category 2", archived = true),
            },
            spendings = {
                    @Spending(category = "Active category 1", description = "Spending with active cat 1", amount = 100, currency = CurrencyValues.RUB),
                    @Spending(category = "Active category 2", description = "Spending with active cat 2", amount = 200, currency = CurrencyValues.EUR),
                    @Spending(category = "Archive category 1", description = "Spending with archive cat 1", amount = 300, currency = CurrencyValues.USD),
                    @Spending(category = "Archive category 2", description = "Spending with archive cat 2", amount = 400, currency = CurrencyValues.KZT)
            }
    )
    @ApiLogin
    public void archivedCategoriesAggregation(@Token String bearerToken) {
        ApolloCall<StatQuery.Data> call = apolloClient.query(StatQuery.builder().build())
                .addHttpHeader("authorization", bearerToken);

        ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(call).blockingGet();

        var value = response.dataOrThrow().stat.statByCategories;

        step("2 активных и 1 архивная категория представлены в списке", () -> assertEquals(3, value.size()));
        step("Последняя категория - Архивная", () -> assertEquals("Archived", value.getLast().categoryName));
    }

    @Test
    @User(
            categories = {
                    @Category(name = "Test category 1"),
                    @Category(name = "Test category 2"),
            },
            spendings = {
                    @Spending(category = "Test category 1", description = "Spending with test cat 1", amount = 100, currency = CurrencyValues.RUB),
                    @Spending(category = "Test category 1", description = "Spending with test cat 1", amount = 200, currency = CurrencyValues.EUR),
                    @Spending(category = "Test category 2", description = "Spending with test cat 2", amount = 300, currency = CurrencyValues.USD),
                    @Spending(category = "Test category 2", description = "Spending with test cat 2", amount = 400, currency = CurrencyValues.RUB)
            }
    )
    @ApiLogin
    public void spendsByFilterIncludeOnlyEuroCurrency(@Token String bearerToken) {
        ApolloCall<StatQuery.Data> call = apolloClient.query(
                        StatQuery.builder()
                                .statCurrency(guru.qa.type.CurrencyValues.EUR)
                                .build())
                .addHttpHeader("authorization", bearerToken);

        ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(call).blockingGet();
        var value = response.dataOrThrow().stat.currency.rawValue;
        step("Полученная валюта при помощи фильтра равна \"Евро\"", () -> assertEquals(CurrencyValues.EUR.name(), value));
    }

    @Test
    @User(
            categories = {
                    @Category(name = "Test category 1"),
                    @Category(name = "Test category 2"),
            },
            spendings = {
                    @Spending(category = "Test category 1", description = "Spending with test cat 1", amount = 100, currency = CurrencyValues.EUR),
                    @Spending(category = "Test category 1", description = "Spending with test cat 1", amount = 200, currency = CurrencyValues.EUR),
                    @Spending(category = "Test category 2", description = "Spending with test cat 2", amount = 300, currency = CurrencyValues.USD),
                    @Spending(category = "Test category 2", description = "Spending with test cat 2", amount = 400, currency = CurrencyValues.KZT)
            }
    )
    @ApiLogin
    public void spendsReturnRubCurrencyByDefault(@Token String bearerToken){
        ApolloCall<StatQuery.Data> call = apolloClient.query(
                        StatQuery.builder()
                                .build())
                .addHttpHeader("authorization", bearerToken);

        ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(call).blockingGet();
        var value = response.dataOrThrow().stat.currency.rawValue;
        step("Полученная валюта по умолчанию \"Рубль\"", () -> assertEquals(CurrencyValues.RUB.name(), value));
    }

    @Test
    @User(
            categories = {
                    @Category(name = "Test category 1"),
                    @Category(name = "Test category 2"),
            },
            spendings = {
                    @Spending(category = "Test category 1", description = "Spending with test cat 1", amount = 100, currency = CurrencyValues.RUB),
                    @Spending(category = "Test category 1", description = "Spending with test cat 1", amount = 200, currency = CurrencyValues.EUR),
                    @Spending(category = "Test category 2", description = "Spending with test cat 2", amount = 300, currency = CurrencyValues.USD),
                    @Spending(category = "Test category 2", description = "Spending with test cat 2", amount = 400, currency = CurrencyValues.EUR)
            }
    )
    @ApiLogin
    public void getSpendsFilteredByCurrency(@Token String bearerToken){
        ApolloCall<StatQuery.Data> call = apolloClient.query(
                        StatQuery.builder()
                                .statCurrency(guru.qa.type.CurrencyValues.EUR)
                                .build())
                .addHttpHeader("authorization", bearerToken);

        ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(call).blockingGet();
        var value = response.dataOrThrow().stat.statByCategories.size();
        step("Количество трат с валютой \"Евро\" равно двум", () -> assertEquals(2, value));
    }

    @Test
    @User
    @ApiLogin
    public void statTest(@Token String bearerToken) {
        ApolloCall<StatQuery.Data> call = apolloClient.query(StatQuery.builder()
                        .filterCurrency(null)
                        .statCurrency(null)
                        .filterPeriod(null)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(call)
                .blockingGet();

        final StatQuery.Data data = response.dataOrThrow();
        StatQuery.Stat result = data.stat;
        assertEquals(
                0.0,
                result.total
        );
    }
}
