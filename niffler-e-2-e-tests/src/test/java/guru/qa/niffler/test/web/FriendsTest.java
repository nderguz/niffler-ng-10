package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.AllPeoplePage;
import guru.qa.niffler.page.FriendsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@DisplayName("Функционал добавления в друзья")
@WebTest
public class FriendsTest {

    @Test
    @User(friends = 3)
    @ApiLogin
    @DisplayName("Должен отображаться список друзей")
    public void friendShouldBePresentInFriendsTable() {
        open(FriendsPage.URL, FriendsPage.class)
                .checkFriendsListIsNotEmpty();
    }

    @Test
    @User
    @ApiLogin
    @DisplayName("Таблица друзей должна быть пустой")
    public void friendsTableShouldBeEmptyForNewUser() {
        open(FriendsPage.URL, FriendsPage.class)
                .checkFriendsListIsEmpty();
    }

    @Test
    @User(incomeInvitations = 2)
    @ApiLogin
    @DisplayName("Должен отображаться входящий запрос на добавление в друзья")
    public void incomeInvitationShouldBePresentInFriendsTable(UserJson user) {
        open(FriendsPage.URL, FriendsPage.class)
                .checkIncomeInvitationShouldBeVisible(user.getTestData().incomeInvitations().getFirst().getUsername());
    }

    @Test
    @User(outcomeInvitations = 3)
    @ApiLogin
    @DisplayName("Статус добавления в друзья должен быть в статусе Waiting...")
    public void outcomeInvitationShouldBePresentInAllPeoplesTable(UserJson user) {
        open(AllPeoplePage.URL, AllPeoplePage.class)
                .checkOutcomeInvitationShouldBeVisible(user.getTestData().outcomeInvitations().getFirst().getUsername());
    }
}
