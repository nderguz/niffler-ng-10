package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@DisplayName("Страница регистрации")
@WebTest
public class RegisterTest {

    private static final Config CFG = Config.getInstance();
    private RegisterPage page;

    @BeforeEach
    public void setUp() {
        page = open(CFG.frontUrl(), LoginPage.class)
                .registerPage();
    }

    @Test
    @DisplayName("Пользователь зарегистрирован корректно")
    public void shouldRegisterNewUser() {
        String username = RandomDataUtils.randomUsername();
        String password = RandomDataUtils.randomPassword();
        page.setUsername(username)
                .setPassword(password)
                .setSubmitPassword(password)
                .successSubmit();
    }

    @Test
    @User
    @DisplayName("Пользователь не зарегистрирован с существующим логином")
    public void shouldNotRegisterUserWithExistingUsername(UserJson user) {
        String existingLogin = user.getUsername();
        String password = "123456";
        page.setUsername(existingLogin)
                .setPassword(password)
                .setSubmitPassword(password)
                .signUp()
                .checkErrorMessage("Username `" + existingLogin + "` already exists");
    }

    @Test
    @DisplayName("Отображена ошибка если пароли не совпадают")
    public void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String username = RandomDataUtils.randomUsername();
        String password = "123456";
        String submitPassword = "654321";
        page.setUsername(username)
                .setPassword(password)
                .setSubmitPassword(submitPassword)
                .signUp()
                .checkErrorMessage("Passwords should be equal");
    }

    @Test
    @DisplayName("Главная страница корректно отображена после успешной регистрации и логина")
    public void mainPageShouldBeDisplayedAfterSuccessLogin() {
        String username = RandomDataUtils.randomUsername();
        String password = RandomDataUtils.randomPassword();
        page.setUsername(username)
                .setPassword(password)
                .setSubmitPassword(password)
                .successSubmit()
                .successLogin(username, password)
                .checkThatPageLoaded();
    }

    //todo исправить проблему с локалью
    @Test
    @DisplayName("Пользователь остается на странице логина после введения некорректных кредов")
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        String username = RandomDataUtils.randomUsername();
        String password = RandomDataUtils.randomPassword();
        String wrongPassword = "12345678";
        page.setUsername(username)
                .setPassword(password)
                .setSubmitPassword(password)
                .successSubmit()
                .badLogin(username, wrongPassword);
    }
}
