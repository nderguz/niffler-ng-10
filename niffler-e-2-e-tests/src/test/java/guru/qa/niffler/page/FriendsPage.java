package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.user.UserJson;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

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

    public FriendsPage checkFriendsListIsEmpty() {
        myFriendsRows.first().shouldNotBe(visible);
        return this;
    }

    public FriendsPage checkFriendsListIsNotEmpty() {
        myFriendsRows.first().shouldBe(visible);
        return this;
    }

    public FriendsPage checkIncomeInvitationShouldBeVisible(List<UserJson> income) {
        friendRequestsRow.findBy(text(friendName))
                .shouldBe(visible)
                .$("button[type='button']")
                .shouldHave(text("Accept"));
        return this;
    }

    public FriendsPage checkOutcomeInvitationShouldBeVisible(List<UserJson> outcome) {
        allPeopleTab.click();
        searchInput.setValue(friendName).pressEnter();
        allPeopleRows.findBy(text(friendName))
                .shouldBe(visible)
                .$(".MuiChip-label")
                .shouldHave(text("Waiting..."));
        return this;
    }

    public FriendsPage checkFriendExistInFriendsList(String friendName){
        //todo реализовать поиск друга в списке друзей
        return this;
    }

    public FriendsPage acceptIncomeInvitation(){

        return this;
    }

    public FriendsPage addFriend(String name){
        //todo реализовать поиск в All people и добавление в друзья
        return this;
    }

}
