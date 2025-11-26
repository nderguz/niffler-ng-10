package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class FriendsTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @User(friends = 3)
    @DisplayName("Должен отображаться список друзей")
    public void friendShouldBePresentInFriendsTable(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .getHeader()
                .toFriendsPage()
                .checkFriendsListIsNotEmpty();
    }

    @Test
    @User
    @DisplayName("Таблица друзей должна быть пустой")
    public void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .getHeader()
                .toFriendsPage()
                .checkFriendsListIsEmpty();
    }

    @Test
    @User(incomeInvitations = 2)
    @DisplayName("Должен отображаться входящий запрос на добавление в друзья")
    public void incomeInvitationShouldBePresentInFriendsTable(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .getHeader()
                .toFriendsPage()
                .checkIncomeInvitationShouldBeVisible(user.getTestData().incomeInvitations().getFirst().getUsername());
    }

    @Test
    @User(outcomeInvitations = 3)
    @DisplayName("Статус добавления в друзья должен быть в статусе Waiting...")
    public void outcomeInvitationShouldBePresentInAllPeoplesTable(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .getHeader()
                .toFriendsPage()
                .checkOutcomeInvitationShouldBeVisible(user.getTestData().incomeInvitations().getFirst().getUsername());
    }
}
