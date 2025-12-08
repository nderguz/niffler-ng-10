package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.io.ClassPathResource;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    public void checkSpendingShouldBeInHistory(UserJson userJson){
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
    public void checkStatComponentTest(UserJson userJson, BufferedImage expected) throws IOException {
        String spendingDescription = userJson.getTestData().spendings().getFirst().description();
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(userJson.getUsername(), userJson.getTestData().password());
        BufferedImage actual = ImageIO.read($("canvas[role='img']").screenshot());
        assertFalse(new ScreenDiffResult(
                expected,
                actual
        ));
    }
}
