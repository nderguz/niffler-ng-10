package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class FriendsPage {

    private final SelenideElement friendsTab = $("a[href='/people/friends']");
    private final SelenideElement allPeopleTab = $("a[href='/people/all']");
    private final SelenideElement friendRequestsTable = $("#requests");
    private final SelenideElement allPeopleTable = $(".MuiTable-root");
    private final SelenideElement myFriendsTable = $("#friends");
    private final SelenideElement searchInput = $("input[aria-label='search']");

    private final ElementsCollection allPeopleRows = allPeopleTable.$$("tbody tr");
    private final ElementsCollection friendRequestsRow = friendRequestsTable.$$("tbody tr");
    private final ElementsCollection myFriendsRows = myFriendsTable.$$("tbody tr");

    @Step("Проверить, что список друзей пуст")
    public @Nonnull FriendsPage checkFriendsListIsEmpty() {
        myFriendsRows.first().shouldNotBe(visible);
        return this;
    }

    @Step("Проверить, что список друзей не пуст")
    public @Nonnull FriendsPage checkFriendsListIsNotEmpty() {
        myFriendsRows.first().shouldBe(visible);
        return this;
    }

    @Step("Проверить входящее приглашение пользователю {username}")
    public @Nonnull FriendsPage checkIncomeInvitationShouldBeVisible(String username) {
        search(username);
        friendRequestsRow.findBy(text(username))
                .shouldBe(visible)
                .$("button[type='button']")
                .shouldHave(text("Accept"));
        return this;
    }

    @Step("Проверить исходящее приглашение пользователю {username}")
    public @Nonnull FriendsPage checkOutcomeInvitationShouldBeVisible(String username) {
        allPeopleTab.click();
        search(username);
        allPeopleRows.findBy(text(username))
                .shouldBe(visible)
                .$(".MuiChip-label")
                .shouldHave(text("Waiting..."));
        return this;
    }

    @Step("Выполнить поиск в поисковой строке: {keyword}")
    public @Nonnull FriendsPage search(String keyword) {
        searchInput.setValue(keyword).pressEnter();
        return this;
    }
}
