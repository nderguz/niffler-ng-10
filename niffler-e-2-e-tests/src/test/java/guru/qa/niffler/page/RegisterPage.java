package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

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

    public RegisterPage setUsername(String username){
        usernameInput.val(username);
        return this;
    }

    public RegisterPage setPassword(String password){
        passwordInput.val(password);
        return this;
    }

    public RegisterPage setSubmitPassword(String password){
        passwordSubmitInput.val(password);
        return this;
    }

    public LoginPage successSubmit(){
        signUp();
        formSingInBtn.click();
        return new LoginPage();
    }

    public RegisterPage checkErrorMessage(String message){
        formError.shouldBe(visible);
        formError.shouldHave(text(message));
        return this;
    }

    public RegisterPage signUp(){
        signUpBtn.click();
        return this;
    }
}
