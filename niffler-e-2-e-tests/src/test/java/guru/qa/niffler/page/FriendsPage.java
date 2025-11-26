package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class FriendsPage {

    private final SelenideElement friendsTab = $("a[href='/people/friends']");
    private final SelenideElement allPeopleTab = $("a[href='/people/all']");
    private final SelenideElement friendRequestsTable = $("#requests");
    private final SelenideElement myFriendsTable = $("#friends");
    private final SelenideElement popup = $("div[role='dialog']");

    private final ElementsCollection requestsRow = friendRequestsTable.$$("tbody tr");
    private final ElementsCollection myFriendsRows = myFriendsTable.$$("tbody tr");

    private final SearchField searchField = new SearchField($("input[aria-label='search']"));
    private final Header header = new Header();

    public @Nonnull Header getHeader() {
        return header;
    }

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

    @Step("Проверить, что список входящих приглашений пуст")
    public @Nonnull FriendsPage checkIncomeInvitationListIsEmpty(){
        requestsRow.first().shouldNotBe(visible);
        return this;
    }

    @Step("Проверить входящее приглашение пользователю {username}")
    public @Nonnull FriendsPage checkIncomeInvitationShouldBeVisible(String username) {
        searchField.search(username);
        requestsRow.findBy(text(username))
                .shouldBe(visible)
                .$("button[type='button']")
                .shouldHave(text("Accept"));
        return this;
    }

    @Step("Принять запрос дружбы от пользователя {incomeInvUsername}")
    public FriendsPage acceptIncomeInvitation(String username) {
        searchField.search(username);
        requestsRow.get(0)
                .find(byText("Accept"))
                .click();
        return this;
    }

    @Step("Отклонить запрос дружбы от пользователя {incomeInvUsername}")
    public FriendsPage declineIncomeInvitation(String username) {
        searchField.search(username);
        requestsRow.get(0)
                .$$("button[type='button']")
                .get(1)
                .click();
        popup.find(byText("Decline")).click();
        return this;
    }
}
