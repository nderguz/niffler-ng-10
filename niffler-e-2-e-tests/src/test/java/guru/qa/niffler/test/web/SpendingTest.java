package guru.qa.niffler.test.web;

import guru.qa.niffler.condition.Color;
import guru.qa.niffler.condition.model.Bubble;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.open;

public class SpendingTest {

    @User(
            spendings = @Spending(
                    category = "Учеба",
                    amount = 89900,
                    currency = CurrencyValues.RUB,
                    description = "Обучение Niffler 2.0 юбилейный поток!"
            )
    )
    @Test
    @ApiLogin
    void spendingDescriptionShouldBeEditedByTableAction(UserJson user) {
        final String newDescription = "Обучение Niffler Next Generation";
        open(MainPage.URL, MainPage.class)
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
    @ApiLogin
    public void checkSpendingShouldBeInHistory(UserJson userJson) {
        String spendingDescription = userJson.getTestData().spendings().getFirst().description();
        open(MainPage.URL, MainPage.class)
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
    @ApiLogin
    @ScreenShotTest(value = "img/expected-stat.png")
    public void checkStatComponentTest(BufferedImage expected) throws IOException {
        open(MainPage.URL, MainPage.class)
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
    @ApiLogin
    @ScreenShotTest(value = "img/spend-removal.png")
    public void checkStatComponentAfterSpendRemove(UserJson user, BufferedImage expected) throws IOException {
        String spendingDescription = user.getTestData().spendings().getFirst().description();
        int categoryCount = user.getTestData().spendings().size();
        open(MainPage.URL, MainPage.class)
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
    @ApiLogin
    @ScreenShotTest(value = "img/spend-edit.png")
    public void checkStatComponentAfterSpendEdit(UserJson user, BufferedImage expected) throws IOException {
        String spendingDescription = user.getTestData().spendings().getFirst().description();
        int categoryCount = user.getTestData().spendings().size();
        open(MainPage.URL, MainPage.class)
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
    @ApiLogin
    @ScreenShotTest(value = "img/spend-archive.png")
    public void checkStatComponentWithArchiveSpend(UserJson user, BufferedImage expected) throws IOException {
        int categoryCount = user.getTestData().spendings().size();
        open(MainPage.URL, MainPage.class)
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

    @User(
            spendings = {@Spending(
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
    @ApiLogin
    @ScreenShotTest(value = "img/spend-rewrite.png")
    public void rewriteExpectedScreenshotTest(UserJson user, BufferedImage expected) throws IOException {
        int categoryCount = user.getTestData().spendings().size();
        open(MainPage.URL, MainPage.class)
                .checkLegendCount(categoryCount)
                .checkLegendNames(user.getTestData().spendings())
                .editSpending(user.getTestData().spendings().getFirst().description())
                .setNewAmount(60000.0)
                .save()
                .assertStatisticsScreenshot(expected);
    }

    @User(
            spendings = {@Spending(
                    category = "Test category 1",
                    amount = 1000,
                    currency = CurrencyValues.RUB,
                    description = "Test description 1"
            ),
                    @Spending(
                            category = "Test category 2",
                            amount = 2000,
                            currency = CurrencyValues.RUB,
                            description = "Test description 2"
                    )}
    )
    @ApiLogin
    @Test
    public void statChipsShouldBeOrderedAndCorrect() {
        open(MainPage.URL, MainPage.class)
                .assertStatBubble(new Bubble(Color.YELLOW, "Test category 2 2000 ₽"), new Bubble(Color.GREEN, "Test category 1 1000 ₽"));
    }

    @User(
            spendings = {@Spending(
                    category = "Test category 1",
                    amount = 1000,
                    currency = CurrencyValues.RUB,
                    description = "Test description 1"
            ),
                    @Spending(
                            category = "Test category 2",
                            amount = 2000,
                            currency = CurrencyValues.RUB,
                            description = "Test description 2"
                    )}
    )
    @ApiLogin
    @Test
    public void statChipsShouldBeInAnyOrderAndCorrect() {
        open(MainPage.URL, MainPage.class)
                .assertStatBubbleInAnyOrder(new Bubble(Color.GREEN, "Test category 1 1000 ₽"), new Bubble(Color.YELLOW, "Test category 2 2000 ₽"));
    }

    @User(
            spendings = {@Spending(
                    category = "Test category 1",
                    amount = 1000,
                    currency = CurrencyValues.RUB,
                    description = "Test description 1"
            ),
                    @Spending(
                            category = "Test category 2",
                            amount = 2000,
                            currency = CurrencyValues.RUB,
                            description = "Test description 2"
                    ),
                    @Spending(
                            category = "Required category",
                            amount = 4000,
                            currency = CurrencyValues.RUB,
                            description = "Test description 5"
                    )}
    )
    @ApiLogin
    @Test
    public void statChipsShouldContains() {
        open(MainPage.URL, MainPage.class)
                .assertStatBubbleContains(new Bubble(Color.BLUE_100, "Test category 1 1000 ₽"));
    }

    @User(
            spendings = {@Spending(
                    category = "Test category 1",
                    amount = 1000,
                    currency = CurrencyValues.RUB,
                    description = "Test description 1"
            ),
                    @Spending(
                            category = "Test category 2",
                            amount = 2000,
                            currency = CurrencyValues.RUB,
                            description = "Test description 2"
                    ),
                    @Spending(
                            category = "Required category",
                            amount = 4000.15,
                            currency = CurrencyValues.RUB,
                            description = "Test description 5"
                    )}
    )
    @ApiLogin
    @Test
    public void statTableShouldContains(UserJson user) {
        open(MainPage.URL, MainPage.class)
                .assertSpendingTable(user.getTestData().spendings());
    }
}
