package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.CalculateRequest;
import guru.qa.niffler.grpc.Currency;
import guru.qa.niffler.grpc.CurrencyValues;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

public class CurrencyGrpcTest extends BaseGrpcTest {

    @Test
    @DisplayName("All 4 currencies are returned")
    public void allCurrenciesShouldReturned() {
        final var allCurrenciesList = getAllCurrencies();
        step("Response contains 4 currencies", () -> assertEquals(4, allCurrenciesList.size()));
    }

    @Test
    @DisplayName("Currencies response structure must be correct")
    public void currencyEnumsShouldBeCorrect() {
        final var allCurrenciesList = getAllCurrencies();
        allCurrenciesList.forEach(c -> {
            step("Currency %s is valid".formatted(c.getCurrency().name()), () -> assertNotEquals("UNSPECIFIED", c.getCurrency().toString()));
            step("Currency rate is valid", () -> assertTrue(c.getCurrencyRate() > 0));
        });
    }

    @Test
    @DisplayName("All currencies should be unique")
    public void allCurrenciesShouldBeUnique() {
        final var allCurrenciesList = getAllCurrencies().stream()
                .map(Currency::getCurrency)
                .toList();
        step("Response contains only unique currencies", () -> assertEquals(allCurrenciesList.size(), new HashSet<>(allCurrenciesList).size()));
    }

    @Test
    @DisplayName("All currencies should return only supported fields")
    public void allCurrenciesShouldReturnOnlySupportedFields() {
        final var allCurrenciesList = currencyServiceBlockingStub.getAllCurrencies(Empty.getDefaultInstance());
        step("Response contract does not contains unknown fields", () -> assertTrue(allCurrenciesList.getUnknownFields().asMap().isEmpty()));
    }

    @MethodSource(value = "calculationRateResult")
    @ParameterizedTest(name = "Calculation result from {0} to {1} and amount {2} should return {3}")
    public void correctRateCalculation(CurrencyValues spendCurrency,
                                       CurrencyValues desiredCurrency,
                                       double amount,
                                       double expected) {
        CalculateRequest request = CalculateRequest.newBuilder()
                .setAmount(amount)
                .setSpendCurrency(spendCurrency)
                .setDesiredCurrency(desiredCurrency)
                .build();
        final var calculateResponse = step("Calculate rate", () -> currencyServiceBlockingStub.calculateRate(request));
        step("Check result", () -> assertEquals(expected, calculateResponse.getCalculatedAmount()));
    }

    private List<Currency> getAllCurrencies() {
        return currencyServiceBlockingStub.getAllCurrencies(Empty.getDefaultInstance()).getAllCurrenciesList();
    }

    private static Stream<Arguments> calculationRateResult() {
        return Stream.of(
                //same
                Arguments.of(CurrencyValues.USD, CurrencyValues.USD, 100.0, 100),
                Arguments.of(CurrencyValues.RUB, CurrencyValues.RUB, 100.0, 100),
                Arguments.of(CurrencyValues.EUR, CurrencyValues.EUR, 100.0, 100),
                Arguments.of(CurrencyValues.KZT, CurrencyValues.KZT, 100.0, 100),
                //from USD
                Arguments.of(CurrencyValues.USD, CurrencyValues.RUB, 100.0, 6666.67),
                Arguments.of(CurrencyValues.USD, CurrencyValues.KZT, 100.0, 47619.05),
                Arguments.of(CurrencyValues.USD, CurrencyValues.EUR, 100.0, 92.59),
                //from RUB
                Arguments.of(CurrencyValues.RUB, CurrencyValues.USD, 100.0, 1.5),
                Arguments.of(CurrencyValues.RUB, CurrencyValues.KZT, 100.0, 714.29),
                Arguments.of(CurrencyValues.RUB, CurrencyValues.EUR, 100.0, 1.39),
                //from EUR
                Arguments.of(CurrencyValues.EUR, CurrencyValues.RUB, 100.0, 7200),
                Arguments.of(CurrencyValues.EUR, CurrencyValues.KZT, 100.0, 51428.57),
                Arguments.of(CurrencyValues.EUR, CurrencyValues.USD, 100.0, 108),
                //from KZT
                Arguments.of(CurrencyValues.KZT, CurrencyValues.RUB, 100.0, 14),
                Arguments.of(CurrencyValues.KZT, CurrencyValues.USD, 100.0, 0.21),
                Arguments.of(CurrencyValues.KZT, CurrencyValues.EUR, 100.0, 0.19)
        );
    }
}
