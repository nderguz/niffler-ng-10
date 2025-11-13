package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitBtn = $("#login-button");
    private final SelenideElement createAccountBtn = $("#register-button");
    private final SelenideElement formError = $(".form__error");

    public MainPage successLogin(String username, String password) {
        usernameInput.val(username);
        passwordInput.val(password);
        submitBtn.click();
        return new MainPage();
    }

    public LoginPage badLogin(String username, String password) {
        usernameInput.val(username);
        passwordInput.val(password);
        submitBtn.click();
        checkErrorMessage("Bad credentials");
        return this;
    }

    public RegisterPage registerPage() {
        createAccountBtn.click();
        return new RegisterPage();
    }

    public void checkErrorMessage(String message) {
        formError.shouldHave(text(message));
    }
}
