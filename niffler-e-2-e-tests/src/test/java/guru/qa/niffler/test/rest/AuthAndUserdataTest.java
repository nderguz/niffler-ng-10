package guru.qa.niffler.test.rest;

import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.impl.UserDbClient;
import org.junit.jupiter.api.Test;

public class AuthAndUserdataTest {

    @Test
    public void createUserWithRepositoryTest() {
        UserClient usersDbClient = new UserDbClient();
        UserJson user = usersDbClient.create(
                "test99", "1234566");
        System.out.println(user);
    }
}
