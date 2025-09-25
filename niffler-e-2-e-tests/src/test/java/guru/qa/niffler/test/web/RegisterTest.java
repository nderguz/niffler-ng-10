package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegisterTest {

    private static final Config CFG = Config.getInstance();
    private final Faker faker = new Faker();

    @Test
    public void shouldRegisterNewUser(){
        String username = faker.name().username();
        String password = faker.name().lastName();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .registerPage()
                .setUsername(username)
                .setPassword(password)
                .setSubmitPassword(password)
                .successSubmit();
    }

    @Test
    public void shouldNotRegisterUserWithExistingUsername(){
        String existingLogin = "test";
        String password = "123456";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .registerPage()
                .setUsername(existingLogin)
                .setPassword(password)
                .setSubmitPassword(password)
                .signUp()
                .checkErrorMessage("Username `" + existingLogin + "` already exists");
    }

    @Test
    public void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(){
        String username = faker.name().username();
        String password = "123456";
        String submitPassword = "654321";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .registerPage()
                .setUsername(username)
                .setPassword(password)
                .setSubmitPassword(submitPassword)
                .signUp()
                .checkErrorMessage("Passwords should be equal");
    }

    @Test
    public void mainPageShouldBeDisplayedAfterSuccessLogin(){
        String username = faker.name().username();
        String password = faker.name().lastName();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .registerPage()
                .setUsername(username)
                .setPassword(password)
                .setSubmitPassword(password)
                .successSubmit()
                .successLogin(username, password)
                .checkThatPageLoaded();
    }

    @Test
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials(){
        String username = faker.name().username();
        String password = faker.name().lastName();
        String wrongPassword = "12345678";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .registerPage()
                .setUsername(username)
                .setPassword(password)
                .setSubmitPassword(password)
                .successSubmit()
                .badLogin(username, wrongPassword);
    }
}
