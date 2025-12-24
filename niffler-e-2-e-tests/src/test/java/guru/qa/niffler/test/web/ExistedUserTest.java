package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.model.user.UserJson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExistedUserTest {

    @Test
    @ApiLogin(username = "test5", password = "test5")
    public void checkExistedUserHasData(UserJson user) {
        assertNotNull(user.getTestData().categories());
        assertNotNull(user.getTestData().spendings());
        assertNotNull(user.getTestData().friends());
        assertNotNull(user.getTestData().incomeInvitations());
        assertNotNull(user.getTestData().outcomeInvitations());
    }
}
