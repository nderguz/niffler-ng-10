package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.impl.UserApiClient;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(1)
@Execution(ExecutionMode.SAME_THREAD)
@RestTest
public class OrderedTest {

    private final UserApiClient userApiClient = new UserApiClient();

    //todo refactor
    @Test
    @User
    public void returnEmptyUserList(UserJson user) {
        var result = userApiClient.allUsers(user.getUsername(), null);
        assertTrue(result.isEmpty());
    }
}
