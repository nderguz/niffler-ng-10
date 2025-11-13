package guru.qa.niffler.test.rest;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.impl.UserDbClient;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class AuthAndUserdataTest {

    @Test
    public void createUserWithRepositoryTest() {
        UserClient usersDbClient = new UserDbClient();
        UserJson user = usersDbClient.create(
                "test99", "1234566");
        System.out.println(user);
    }

    @Test
    public void addIncomeRequestTest() {
        UserClient userDbClient = new UserDbClient();
        userDbClient.addInvitation(
                new UserJson(
                        UUID.fromString("13655eb2-b5a1-11f0-a7fa-5af73ba378f7"),
                        "test-11",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                ),
                new UserJson(
                        UUID.fromString("fd4cff92-b59e-11f0-9162-5af73ba378f7"),
                        "test-10",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
    }

    @Test
    public void addFriendTest() {
        UserClient userDbClient = new UserDbClient();
        userDbClient.addFriend(
                new UserJson(
                        UUID.fromString("fd4cff92-b59e-11f0-9162-5af73ba378f7"),
                        "test-10",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                ),
                new UserJson(
                        UUID.fromString("13655eb2-b5a1-11f0-a7fa-5af73ba378f7"),
                        "test-11",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
    }
}
