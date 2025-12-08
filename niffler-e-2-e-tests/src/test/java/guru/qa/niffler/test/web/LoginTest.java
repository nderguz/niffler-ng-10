package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@DisplayName("Страница логина")
public class LoginTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @DisabledByIssue("3")
    @User
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkThatPageLoaded();
    }
}
