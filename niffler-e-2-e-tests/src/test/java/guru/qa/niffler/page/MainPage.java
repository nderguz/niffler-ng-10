package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

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

    public MainPage checkThatPageLoaded() {
        spendingTable.shouldBe(visible);
        statistics.shouldBe(visible);
        return this;
    }

    public FriendsPage openFriendsPage(){
        menuBtn.click();
        menuOptions.find(text("Friends")).click();
        return new FriendsPage();
    }

    public EditSpendingPage editSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public MainPage checkThatTableContains(String description) {
        spendingTable.$$("tbody tr").find(text(description)).should(visible);
        return this;
    }

    public ProfilePage openProfilePage() {
        menuBtn.click();
        menuOptions.find(text("Profile")).click();
        return new ProfilePage();
    }

    public MainPage search(String inputText) {
        searchInput.val(inputText).pressEnter();
        return this;
    }
}
