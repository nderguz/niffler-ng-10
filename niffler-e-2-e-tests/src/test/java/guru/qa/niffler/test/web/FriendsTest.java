package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.jupiter.annotation.UsersQueue;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.annotation.UserType.Type.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;

@ExtendWith(BrowserExtension.class)
public class FriendsTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @UsersQueue
    @DisplayName("Должен отображаться список друзей")
    public void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.password())
                .openFriendsPage()
                .checkFriendsListIsNotEmpty();
    }

    @Test
    @UsersQueue
    @DisplayName("Таблица друзей должна быть пустой")
    public void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.password())
                .openFriendsPage()
                .checkFriendsListIsEmpty();
    }

    @Test
    @UsersQueue
    @DisplayName("Должен отображаться входящий запрос на добавление в друзья")
    public void incomeInvitationShouldBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.password())
                .openFriendsPage()
                .checkIncomeInvitationShouldBeVisible();
    }

    @Test
    @UsersQueue
    @DisplayName("Статус добавления в друзья должен быть в статусе Waiting...")
    public void outcomeInvitationShouldBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.password())
                .openFriendsPage()
                .checkOutcomeInvitationShouldBeVisible("test3");
    }
}
