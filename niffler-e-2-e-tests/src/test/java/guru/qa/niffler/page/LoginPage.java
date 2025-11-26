package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class LoginPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitBtn = $("#login-button");
    private final SelenideElement createAccountBtn = $("#register-button");
    private final SelenideElement formError = $(".form__error");

    @Step("Авторизоваться с валидными кредами")
    public @Nonnull MainPage successLogin(String username, String password) {
        usernameInput.val(username);
        passwordInput.val(password);
        submitBtn.click();
        return new MainPage();
    }

    @Step("Авторизоваться с невалидными кредами")
    public @Nonnull LoginPage badLogin(String username, String password) {
        usernameInput.val(username);
        passwordInput.val(password);
        submitBtn.click();
        checkErrorMessage("Bad credentials");
        return this;
    }

    @Step("Открыть страницу регистрации")
    public @Nonnull RegisterPage registerPage() {
        createAccountBtn.click();
        return new RegisterPage();
    }

    @Step("Проверить наличие текста ошибки: {message}")
    public void checkErrorMessage(String message) {
        formError.shouldHave(text(message));
    }
}
