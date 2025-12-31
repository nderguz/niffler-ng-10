package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;


@DisplayName("Тесты для нового пользователя")
@WebTest
public class NewUserTest {

    @Test
    @DisplayName("Принятие заявки в друзья")
    @User(incomeInvitations = 1)
    @ApiLogin
    public void acceptIncomeInvitation(UserJson user) {
        var incomeInvUsername = user.getTestData().incomeInvitations().getFirst().getUsername();
        open(FriendsPage.URL, FriendsPage.class)
                .acceptIncomeInvitation(incomeInvUsername)
                .checkFriendsListIsNotEmpty();
    }

    @Test
    @DisplayName("Отклонение заявки в друзья")
    @User(incomeInvitations = 1)
    @ApiLogin
    public void declineIncomeInvitation(UserJson user) {
        var incomeInvUsername = user.getTestData().incomeInvitations().getFirst().getUsername();
        open(FriendsPage.URL, FriendsPage.class)
                .declineIncomeInvitation(incomeInvUsername)
                .checkIncomeInvitationListIsEmpty();
    }

    @Test
    @DisplayName("Добавление новой траты")
    @User
    @ApiLogin
    public void addNewSpending(UserJson user) {
        var spending = RandomDataUtils.randomSpend(user.getUsername());
        open(EditSpendingPage.URL, EditSpendingPage.class)
                .setNewAmount(spending.amount())
                .setNewCategory(spending.category().name())
                .setNewSpendingDescription(spending.description())
                .save()
                .checkThatTableContains(spending.description());
    }

    @Test
    @DisplayName("Редактирование профиля")
    @User
    @ApiLogin
    public void editProfile() {
        var newUsername = RandomDataUtils.randomUsername();
        var newCategoryName = RandomDataUtils.randomCategoryName();
        open(ProfilePage.URL, ProfilePage.class)
                .checkUsernameDisabled()
                .setNewName(newUsername)
                .saveChanges()
                .checkName(newUsername)
                .addCategory(newCategoryName)
                .checkCategoryExists(newCategoryName);
    }
}