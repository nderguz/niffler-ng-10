package guru.qa.niffler.test.rest;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcTest {

    @Test
    public void daoTest(){
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend =  spendDbClient.createSpend(
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
}
