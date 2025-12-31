package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.service.impl.UserDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

@RestTest
public class JdbcTest {

    @Test
    public void txTest() {
        SpendClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "test-cat-name",
                                "test",
                                false
                        ),
                        CurrencyValues.RUB,
                        100.0,
                        "test desc",
                        "test"
                )
        );

        System.out.println(spend);
    }

    @Test
    //todo refactor
    public void springJdbcTest() {
        UserClient usersDbClient = new UserDbClient();
        usersDbClient.create(
                "test", "12345");
    }
}
