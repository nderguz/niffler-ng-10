package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public abstract class BasePage<T extends BasePage<?>> {

    protected final Header header = new Header();
    protected final SelenideElement snackbar = $(".MuiAlert-message");

    @SuppressWarnings("unchecked")
    @Step("Проверить сообщение алерта: {}")
    @Nonnull
    public T checkSnackbarText(String text) {
        snackbar.shouldHave(text(text));
        return (T) this;
    }

    public @Nonnull Header getHeader() {
        return header;
    }
}
