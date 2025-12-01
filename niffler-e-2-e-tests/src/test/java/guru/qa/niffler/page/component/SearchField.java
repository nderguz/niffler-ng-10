package guru.qa.niffler.page.component;

import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchField extends BaseComponent<SearchField> {

    public SearchField() {
        super($("input[aria-label='search']"));
    }

    @Step("Выполнение поиска в поисковой строке: {query}")
    public @Nonnull SearchField search(String query) {
        self.setValue(query).pressEnter();
        return this;
    }

    @Step("Очистка поисковой строки")
    public @Nonnull SearchField clearIfNotEmpty() {
        self.setValue("");
        return this;
    }
}