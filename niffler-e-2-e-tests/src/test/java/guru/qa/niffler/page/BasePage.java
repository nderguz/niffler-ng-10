package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;

@ParametersAreNonnullByDefault
public abstract class BasePage<T extends BasePage<?>> {

    protected final Header header = new Header();
    protected final SelenideElement alert;

    protected BasePage(SelenideDriver driver) {
        this.alert = driver.$(".MuiAlert-message");
    }

    protected BasePage() {
        this.alert = Selenide.$(".MuiAlert-message");
    }

    @SuppressWarnings("unchecked")
    @Step("Проверить сообщение алерта: {text}")
    @Nonnull
    public T checkAlertText(String text) {
        alert.shouldHave(text(text));
        return (T) this;
    }

    public @Nonnull Header getHeader() {
        return header;
    }
}
