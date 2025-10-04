package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(BrowserExtension.class)
public class SpendingTest {

    private static final Config CFG = Config.getInstance();

    @User(
            username = "test",
            spendings =     @Spending(
                    category = "Учеба",
                    amount = 89900,
                    currency = CurrencyValues.RUB,
                    description = "Обучение Niffler 2.0 юбилейный поток!"
            )
    )
    @Test
    void spendingDescriptionShouldBeEditedByTableAction(SpendJson spending) {
        final String newDescription = "Обучение Niffler Next Generation";
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin("test", "test")
                .editSpending(spending.description())
                .setNewSpendingDescription(newDescription)
                .save()
                .checkThatTableContains(newDescription);
    }
}
