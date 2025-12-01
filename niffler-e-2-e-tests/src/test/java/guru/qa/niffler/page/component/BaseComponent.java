package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Getter(AccessLevel.PROTECTED)
public abstract class BaseComponent<T extends  BaseComponent<T>> {

    protected final SelenideElement self;

    public BaseComponent(SelenideElement self) {
        this.self = self;
    }
}
