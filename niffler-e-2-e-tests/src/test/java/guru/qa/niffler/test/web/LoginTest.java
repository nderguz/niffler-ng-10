package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.SelenideUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Страница логина")
public class LoginTest {

    private static final Config CFG = Config.getInstance();

    private final SelenideDriver driver = new SelenideDriver(SelenideUtils.CHROME_CONFIG);

    @Test
    @DisabledByIssue("3")
    @User
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        driver.open(CFG.frontUrl());
        new LoginPage()
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkThatPageLoaded();
    }
}
