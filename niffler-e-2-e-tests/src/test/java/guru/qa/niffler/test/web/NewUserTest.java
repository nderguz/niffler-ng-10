package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class NewUserTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @DisplayName("Принятие заявки в друзья")
    @User(incomeInvitations = 1)
    public void acceptIncomeInvitation(UserJson user) {
        var incomeInvUsername = user.getTestData().incomeInvitations().getFirst().getUsername();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toFriendsPage()
                .acceptIncomeInvitation(incomeInvUsername)
                .checkFriendsListIsNotEmpty();
    }

    @Test
    @DisplayName("Отклонение заявки в друзья")
    @User(incomeInvitations = 1)
    public void declineIncomeInvitation(UserJson user) {
        var incomeInvUsername = user.getTestData().incomeInvitations().getFirst().getUsername();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toFriendsPage()
                .declineIncomeInvitation(incomeInvUsername)
                .checkIncomeInvitationListIsEmpty();
    }

    @Test
    @DisplayName("Добавление новой траты")
    @User
    public void addNewSpending(UserJson user) {
        var spending = RandomDataUtils.randomSpend(user.getUsername());
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toAddSpendingPage()
                .setNewAmount(spending.amount())
                .setNewCategory(spending.category().name())
                .setNewSpendingDescription(spending.description())
                .save()
                .checkThatTableContains(spending.description());
    }

    @Test
    @DisplayName("Редактирование профиля")
    @User
    public void editProfile(UserJson user) {
        var newUsername = RandomDataUtils.randomUsername();
        var newCategoryName = RandomDataUtils.randomCategoryName();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .checkUsernameDisabled()
                .setNewName(newUsername)
                .saveChanges()
                .checkName(newUsername)
                .addCategory(newCategoryName)
                .checkCategoryExists(newCategoryName);

    }
}