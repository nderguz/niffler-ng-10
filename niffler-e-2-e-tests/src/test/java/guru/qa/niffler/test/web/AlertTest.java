package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@DisplayName("Проверки алертов на всех страницах")
public class AlertTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @User
    @DisplayName("Проверка алерта в профиле после смены имени")
    public void changeNameAlert(UserJson user) {
        var newName = RandomDataUtils.randomName();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .setNewName(newName)
                .saveChanges()
                .checkAlertText("Profile successfully updated");
    }

    @Test
    @User
    @DisplayName("Проверка алерта в профиле после добавления категории")
    public void addNewCategoryAlert(UserJson user) {
        var newCategory = RandomDataUtils.randomCategoryName();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .addCategory(newCategory)
                .checkAlertText("You've added new category: %s".formatted(newCategory));
    }

    @Test
    @User(categories = @Category(name = "Test category"))
    @DisplayName("Проверка алерта в профиле после изменения имени категории")
    public void changeCategoryNameAlert(UserJson user) {
        var newCategoryName = RandomDataUtils.randomCategoryName();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .changeCategoryName(user.getTestData().categories().getFirst().name(), newCategoryName)
                .checkAlertText("Category name is changed");
    }

    @Test
    @User(categories = @Category(name = "Test category"))
    @DisplayName("Проверка алерта в профиле после добавления категории в архив")
    public void addCategoryToArchiveAlert(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .addCategoryToArchive()
                .checkAlertText("Category %s is archived".formatted(user.getTestData().categories().getFirst().name()));
    }

    @Test
    @DisplayName("Проверка алерта после добавления нового спендинга")
    @User
    public void addSpendingAlert(UserJson user) {
        var spending = RandomDataUtils.randomSpend(user.getUsername());
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .getHeader()
                .toAddSpendingPage()
                .setNewAmount(spending.amount())
                .setNewCategory(spending.category().name())
                .setNewSpendingDescription(spending.description())
                .save()
                .checkAlertText("New spending is successfully created");
    }

    @Test
    @User(spendings = @Spending (
            category = "TestCategory",
            amount = 10000,
            currency = CurrencyValues.RUB,
            description = "Test category description"
    ))
    @DisplayName("Проверка алерта после изменения спендинга")
    public void changeSpendingAlert(UserJson user) {
        var spending = RandomDataUtils.randomSpend(user.getUsername());
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .editSpending(user.getTestData().spendings().getFirst().description())
                .setNewAmount(spending.amount())
                .setNewCategory(spending.category().name())
                .setNewSpendingDescription(spending.description())
                .save()
                .checkAlertText("Spending is edited successfully");
    }

    @Test
    @User(spendings = @Spending (
            category = "TestCategory",
            amount = 10000,
            currency = CurrencyValues.RUB,
            description = "Test category description"
    ))
    @DisplayName("Проверка алерта после изменения спендинга")
    public void deleteSpendingAlert(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .deleteSpending(user.getTestData().spendings().getFirst().description())
                .checkAlertText("Spendings succesfully deleted");
    }

    @Test
    @User(incomeInvitations = 1)
    @DisplayName("Проверка алерта после добавления в друзья")
    public void addFriendAlert(UserJson user) {
        var incomeInvUsername = user.getTestData().incomeInvitations().getFirst().getUsername();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .getHeader()
                .toFriendsPage()
                .acceptIncomeInvitation(incomeInvUsername)
                .checkAlertText("Invitation of %s accepted".formatted(incomeInvUsername));
    }

    @Test
    @User(friends = 1)
    @DisplayName("Проверка алерта после удаления из друзей")
    public void unfriendAlert(UserJson user) {
        var friendNickname = user.getTestData().friends().getFirst().getUsername();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .getHeader()
                .toFriendsPage()
                .removeFriend(friendNickname)
                .checkAlertText("Friend %s is deleted".formatted(friendNickname));
    }
}
