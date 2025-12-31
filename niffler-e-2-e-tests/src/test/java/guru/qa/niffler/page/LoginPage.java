package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {

    public static final String URL = CFG.authUrl() + "login";
    private final SelenideElement usernameInput;
    private final SelenideElement passwordInput;
    private final SelenideElement submitBtn;
    private final SelenideElement createAccountBtn;
    private final SelenideElement formError;

    public LoginPage(SelenideDriver driver) {
        super(driver);
        this.usernameInput = driver.$("#username");
        this.passwordInput = driver.$("#password");
        this.submitBtn = driver.$("#login-button");
        this.createAccountBtn = driver.$("#register-button");
        this.formError = driver.$(".form__error");
    }

    public LoginPage() {
        this.usernameInput = Selenide.$("#username");
        this.passwordInput = Selenide.$("#password");
        this.submitBtn = Selenide.$("#login-button");
        this.createAccountBtn = Selenide.$("#register-button");
        this.formError = Selenide.$(".form__error");
    }

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
        checkErrorMessage("Неверные учетные данные пользователя");
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
