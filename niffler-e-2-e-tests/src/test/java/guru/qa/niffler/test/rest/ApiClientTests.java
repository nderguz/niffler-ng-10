package guru.qa.niffler.test.rest;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.service.impl.UserApiClient;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class ApiClientTests {

    private static final Config CFG = Config.getInstance();

    @Test
    @User(outcomeInvitations = 15)
    public void addIncomeInvitationTest(UserJson userJson) {
        //todo проверить, что методы в UserApi работают
        UserApiClient userApiClient = new UserApiClient();

        //todo дописать проверку на случайного входящего друга
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(userJson.getUsername(), userJson.getTestData().password())
                .checkThatPageLoaded()
                .openFriendsPage();
    }
}
