package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@DisplayName("Проверки алертов на всех страницах")
public class AlertTest {

    @Test
    @User
    @ApiLogin
    @DisplayName("Проверка алерта в профиле после смены имени")
    public void changeNameAlert() {
        var newName = RandomDataUtils.randomName();
        open(ProfilePage.URL, ProfilePage.class)
                .setNewName(newName)
                .saveChanges()
                .checkAlertText("Profile successfully updated");
    }

    @Test
    @User
    @ApiLogin
    @DisplayName("Проверка алерта в профиле после добавления категории")
    public void addNewCategoryAlert() {
        var newCategory = RandomDataUtils.randomCategoryName();
        open(ProfilePage.URL, ProfilePage.class)
                .addCategory(newCategory)
                .checkAlertText("You've added new category: %s".formatted(newCategory));
    }

    @Test
    @User(categories = @Category(name = "Test category"))
    @ApiLogin
    @DisplayName("Проверка алерта в профиле после изменения имени категории")
    public void changeCategoryNameAlert(UserJson user) {
        var newCategoryName = RandomDataUtils.randomCategoryName();
        open(ProfilePage.URL, ProfilePage.class)
                .changeCategoryName(user.getTestData().categories().getFirst().name(), newCategoryName)
                .checkAlertText("Category name is changed");
    }

    @Test
    @User(categories = @Category(name = "Test category"))
    @ApiLogin
    @DisplayName("Проверка алерта в профиле после добавления категории в архив")
    public void addCategoryToArchiveAlert(UserJson user) {
        open(ProfilePage.URL, ProfilePage.class)
                .addCategoryToArchive()
                .checkAlertText("Category %s is archived".formatted(user.getTestData().categories().getFirst().name()));
    }

    @Test
    @User
    @ApiLogin
    @DisplayName("Проверка алерта после добавления нового спендинга")
    public void addSpendingAlert(UserJson user) {
        var spending = RandomDataUtils.randomSpend(user.getUsername());
        open(EditSpendingPage.URL, EditSpendingPage.class)
                .setNewAmount(spending.amount())
                .setNewCategory(spending.category().name())
                .setNewSpendingDescription(spending.description())
                .save()
                .checkAlertText("New spending is successfully created");
    }

    @Test
    @User(spendings = @Spending(
            category = "TestCategory",
            amount = 10000,
            currency = CurrencyValues.RUB,
            description = "Test category description"
    ))
    @ApiLogin
    @DisplayName("Проверка алерта после изменения спендинга")
    public void changeSpendingAlert(UserJson user) {
        var spending = RandomDataUtils.randomSpend(user.getUsername());
        open(MainPage.URL, MainPage.class)
                .editSpending(user.getTestData().spendings().getFirst().description())
                .setNewAmount(spending.amount())
                .setNewCategory(spending.category().name())
                .setNewSpendingDescription(spending.description())
                .save()
                .checkAlertText("Spending is edited successfully");
    }

    @Test
    @User(spendings = @Spending(
            category = "TestCategory",
            amount = 10000,
            currency = CurrencyValues.RUB,
            description = "Test category description"
    ))
    @ApiLogin
    @DisplayName("Проверка алерта после изменения спендинга")
    public void deleteSpendingAlert(UserJson user) {
        open(MainPage.URL, MainPage.class)
                .deleteSpending(user.getTestData().spendings().getFirst().description())
                .checkAlertText("Spendings succesfully deleted");
    }

    @Test
    @User(incomeInvitations = 1)
    @ApiLogin
    @DisplayName("Проверка алерта после добавления в друзья")
    public void addFriendAlert(UserJson user) {
        var incomeInvUsername = user.getTestData().incomeInvitations().getFirst().getUsername();
        open(FriendsPage.URL, FriendsPage.class)
                .acceptIncomeInvitation(incomeInvUsername)
                .checkAlertText("Invitation of %s accepted".formatted(incomeInvUsername));
    }

    @Test
    @User(friends = 1)
    @ApiLogin
    @DisplayName("Проверка алерта после удаления из друзей")
    public void unfriendAlert(UserJson user) {
        var friendNickname = user.getTestData().friends().getFirst().getUsername();
        open(FriendsPage.URL, FriendsPage.class)
                .removeFriend(friendNickname)
                .checkAlertText("Friend %s is deleted".formatted(friendNickname));
    }
}
