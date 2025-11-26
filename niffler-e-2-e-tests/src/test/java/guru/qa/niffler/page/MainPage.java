package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class MainPage {
    private final SelenideElement spendingTable = $("#spendings");
    private final SelenideElement statistics = $("#stat");
    private final SelenideElement spendings = $("#spendings");
    private final SelenideElement menuBtn = $("button[aria-label='Menu']");
    private final ElementsCollection menuOptions = $$("li a");
    private final SelenideElement searchInput = $("input[aria-label='search']");

    @Step("Главная страница загрузилась корректно")
    public @Nonnull MainPage checkThatPageLoaded() {
        spendingTable.shouldBe(visible);
        statistics.shouldBe(visible);
        return this;
    }

    @Step("Открыть страницу со списком друзей")
    public @Nonnull FriendsPage openFriendsPage(){
        menuBtn.click();
        menuOptions.find(text("Friends")).click();
        return new FriendsPage();
    }

    @Step("Нажать на трату {description}")
    public @Nonnull EditSpendingPage editSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Step("Проверить, что в тратах существует {description}")
    public @Nonnull MainPage checkThatTableContains(String description) {
        spendingTable.$$("tbody tr").find(text(description)).should(visible);
        return this;
    }

    @Step("Открыть профиль пользователя")
    public @Nonnull ProfilePage openProfilePage() {
        menuBtn.click();
        menuOptions.find(text("Profile")).click();
        return new ProfilePage();
    }

    @Step("Выполнить поиск в поисковой строке: {inputText}")
    public @Nonnull MainPage search(String inputText) {
        searchInput.val(inputText).pressEnter();
        return this;
    }
}
