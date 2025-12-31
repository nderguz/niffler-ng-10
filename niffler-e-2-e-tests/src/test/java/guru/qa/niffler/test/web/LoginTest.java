package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.Driver;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.Browser;
import guru.qa.niffler.jupiter.extension.NonStaticBrowserExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Страница логина")
@WebTest
public class LoginTest {

    @RegisterExtension
    private static final NonStaticBrowserExtension nonStaticBrowserExtension = new NonStaticBrowserExtension();
    private static final Config CFG = Config.getInstance();

    /*
        Тест запукается 2 раза параллельно в двух разных браузерах
     */
    //todo refactor
    @DisabledByIssue("3")
    @User
    @EnumSource(value = Browser.class, names = {"CHROME", "FIREFOX"})
    @ParameterizedTest
    public void mainPageShouldBeDisplayedAfterSuccessLogin(@Driver SelenideDriver driver, UserJson user) {
        NonStaticBrowserExtension.drivers().add(driver);
        driver.open(CFG.frontUrl());
        new LoginPage(driver)
                .successLogin(user.getUsername(), user.getTestData().password());
        new MainPage(driver).checkThatPageLoaded();
    }
}
