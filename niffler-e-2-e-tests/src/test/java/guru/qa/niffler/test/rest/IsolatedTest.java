package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.impl.UserApiClient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Order(Integer.MAX_VALUE)
@RestTest
public class IsolatedTest {

    private final UserApiClient userApiClient = new UserApiClient();

    @Test
    @User
    public void returnNonEmptyUserList(UserJson user) {
        var result = userApiClient.allUsers(user.getUsername(), null);
        assertFalse(result.isEmpty());
    }
}
