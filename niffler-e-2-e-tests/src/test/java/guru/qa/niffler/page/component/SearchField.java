package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SearchField {

    private final SelenideElement self;

    public SearchField(SelenideElement self) {
        this.self = self;
    }

    @Step("Выполнение поиска в поисковой строке: {query}")
    public @Nonnull SearchField search(String query){
        self.setValue(query).pressEnter();
        return new SearchField(self);
    }

    @Step("Очистка поисковой строки")
    public @Nonnull SearchField clearIfNotEmpty(){
        self.setValue("");
        return new SearchField(self);
    }
}