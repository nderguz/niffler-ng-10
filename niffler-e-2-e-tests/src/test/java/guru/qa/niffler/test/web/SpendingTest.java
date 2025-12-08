package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SpendingTest {

    private static final Config CFG = Config.getInstance();

    @User(
            spendings = @Spending(
                    category = "Учеба",
                    amount = 89900,
                    currency = CurrencyValues.RUB,
                    description = "Обучение Niffler 2.0 юбилейный поток!"
            )
    )
    @Test
    void spendingDescriptionShouldBeEditedByTableAction(UserJson user) {
        final String newDescription = "Обучение Niffler Next Generation";
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .editSpending(user.getTestData().spendings().getFirst().description())
                .setNewSpendingDescription(newDescription)
                .save()
                .checkThatTableContains(newDescription);
    }

    @Test
    @User(
            spendings = @Spending(
                    category = "Учеба",
                    amount = 89900,
                    currency = CurrencyValues.RUB,
                    description = "Обучение Niffler 2.0 юбилейный поток!"
            )
    )
    public void checkSpendingShouldBeInHistory(UserJson userJson) {
        String spendingDescription = userJson.getTestData().spendings().getFirst().description();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(userJson.getUsername(), userJson.getTestData().password())
                .checkThatPageLoaded()
                .search(spendingDescription)
                .checkThatTableContains(spendingDescription);
    }

    @User(
            spendings = @Spending(
                    category = "Учеба",
                    amount = 79990,
                    currency = CurrencyValues.RUB,
                    description = "Обучение Niffler 2.0 юбилейный поток!"
            )
    )
    @ScreenShotTest("img/expected-stat.png")
    public void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException {
        String spendingDescription = user.getTestData().spendings().getFirst().description();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .assertStatisticsScreenshot(expected);
    }

    @User(
            spendings = {
                    @Spending(
                            category = "Test category 1",
                            amount = 1000,
                            currency = CurrencyValues.RUB,
                            description = "Test description 1"
                    ),
                    @Spending(
                            category = "Test category 2",
                            amount = 1000,
                            currency = CurrencyValues.RUB,
                            description = "Test description 2"
                    )
            }
    )
    @ScreenShotTest("img/spend-removal.png")
    public void checkStatComponentAfterSpendRemove(UserJson user, BufferedImage expected) throws IOException {
        String spendingDescription = user.getTestData().spendings().getFirst().description();
        int categoryCount = user.getTestData().spendings().size();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkLegendCount(categoryCount)
                .checkLegendNames(user.getTestData().spendings())
                .deleteSpending(spendingDescription)
                .checkLegendCount(categoryCount - 1)
                .assertStatisticsScreenshot(expected);
    }

    @User(
            spendings = {
                    @Spending(
                            category = "Test category 1",
                            amount = 1000,
                            currency = CurrencyValues.RUB,
                            description = "Test description 1"
                    ),
                    @Spending(
                            category = "Test category 2",
                            amount = 1000,
                            currency = CurrencyValues.RUB,
                            description = "Test description 2"
                    )
            }
    )
    @ScreenShotTest("img/spend-edit.png")
    public void checkStatComponentAfterSpendEdit(UserJson user, BufferedImage expected) throws IOException {
        String spendingDescription = user.getTestData().spendings().getFirst().description();
        int categoryCount = user.getTestData().spendings().size();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkLegendCount(categoryCount)
                .checkLegendNames(user.getTestData().spendings())
                .editSpending(spendingDescription)
                .setNewAmount(1500.0)
                .save()
                .checkLegendCount(categoryCount + 1)
                .assertStatisticsScreenshot(expected);
    }

    @User(
            spendings = {
                    @Spending(
                            category = "Test category 1",
                            amount = 1000,
                            currency = CurrencyValues.RUB,
                            description = "Test description 1"
                    ),
                    @Spending(
                            category = "Test category 2",
                            amount = 1000,
                            currency = CurrencyValues.RUB,
                            description = "Test description 2"
                    )
            }
    )
    @ScreenShotTest("img/spend-archive.png")
    @Test
    public void checkStatComponentWithArchiveSpend(UserJson user, BufferedImage expected) throws IOException {
        int categoryCount = user.getTestData().spendings().size();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkLegendCount(categoryCount)
                .checkLegendNames(user.getTestData().spendings())
                .getHeader()
                .toProfilePage()
                .addCategoryToArchive()
                .getHeader()
                .toMainPage()
                .checkLegendCount(categoryCount)
                .checkArchiveLegendName()
                .assertStatisticsScreenshot(expected);
    }
}
