package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpBtn = $("#register-button");
    private final SelenideElement formSingInBtn = $(".form_sign-in");
    private final SelenideElement formError = $(".form__error");

    @Step("Ввести имя пользователя: {username}")
    public @Nonnull RegisterPage setUsername(String username){
        usernameInput.val(username);
        return this;
    }

    @Step("Ввести пароль: {password}")
    public @Nonnull RegisterPage setPassword(String password){
        passwordInput.val(password);
        return this;
    }

    @Step("Ввести пароль повторно: {password}")
    public @Nonnull RegisterPage setSubmitPassword(String password){
        passwordSubmitInput.val(password);
        return this;
    }

    @Step("Нажать на кнопку \"Sign up\"")
    public @Nonnull LoginPage successSubmit(){
        signUp();
        formSingInBtn.click();
        return new LoginPage();
    }

    @Step("Проверить наличие сообщения об ошибке: {message}")
    public @Nonnull RegisterPage checkErrorMessage(String message){
        formError.shouldBe(visible);
        formError.shouldHave(text(message));
        return this;
    }

    @Step("Нажать на кнопку \"Sign in\"")
    public @Nonnull RegisterPage signUp(){
        signUpBtn.click();
        return this;
    }
}
