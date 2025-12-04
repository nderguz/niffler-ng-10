package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.component.SpendingTable;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;


import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {
    private final SelenideElement statistics = $("#stat");
    private final SpendingTable spendings = new SpendingTable();
    private final SearchField searchField = new SearchField();

    private final SpendingTable spendingTable = new SpendingTable();

    @Step("Главная страница загрузилась корректно")
    public @Nonnull MainPage checkThatPageLoaded() {
        statistics.shouldBe(visible);
        return this;
    }

    @Step("Нажать на трату {description}")
    public @Nonnull EditSpendingPage editSpending(String description) {
        spendingTable.editSpending(description);
        return new EditSpendingPage();
    }

    @Step("Проверить, что в тратах существует {description}")
    public @Nonnull MainPage checkThatTableContains(String description) {
        spendingTable.checkTableContains(description);
        return this;
    }

    @Step("Выполнить поиск в поисковой строке: {inputText}")
    public @Nonnull MainPage search(String inputText) {
        searchField.search(inputText);
        return this;
    }

    public @Nonnull MainPage deleteSpending(String description) {
        spendings.deleteSpending(description);
        return this;
    }
}
