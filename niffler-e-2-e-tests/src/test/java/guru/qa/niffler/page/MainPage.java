package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {
    private final SelenideElement statistics = $("#stat");
    private final ElementsCollection statisticsLegend = $$("div[id='legend-container'] ul");
    private final SpendingTable spendings = new SpendingTable();
    private final SearchField searchField = new SearchField();

    private final SpendingTable spendingTable = new SpendingTable();

    @Step("Главная страница загрузилась корректно")
    public @Nonnull MainPage checkThatPageLoaded() {
        statistics.shouldBe(visible);
        return this;
    }

    @Step("Нажать на трату {description}")
    public @Nonnull EditSpendingPage editSpending(String description) {
        spendingTable.editSpending(description);
        return new EditSpendingPage();
    }

    @Step("Проверить кол-во категорий в легенде под статистикой равно {count}")
    public @Nonnull MainPage checkLegendCount(int count) {
        Objects.equals(statisticsLegend.size(), count);
        return this;
    }

    @Step("Корректное содержаение легенды под статистикой")
    public @Nonnull MainPage checkLegendNames(List<SpendJson> spends) {
        for (SpendJson spend : spends) {
            statisticsLegend.find(text(generateStatLegendName(spend)));
        }
        return this;
    }

    @Step("Легенда содержит категорию \"Архив\"")
    public @Nonnull MainPage checkArchiveLegendName() {
        statisticsLegend.find(text("Архив"));
        return this;
    }

    @Step("Проверить, что в тратах существует {description}")
    public @Nonnull MainPage checkThatTableContains(String description) {
        spendingTable.checkTableContains(description);
        return this;
    }

    @Step("Выполнить поиск в поисковой строке: {inputText}")
    public @Nonnull MainPage search(String inputText) {
        searchField.search(inputText);
        return this;
    }

    public @Nonnull MainPage deleteSpending(String description) {
        spendings.deleteSpending(description);
        return this;
    }

    @Step("Сравнение скриншотов")
    public void assertStatisticsScreenshot(BufferedImage expected) throws IOException {
        Selenide.sleep(4000);
        BufferedImage actual = ImageIO.read($("canvas[role='img']").screenshot());
        assertFalse(new ScreenDiffResult(
                expected,
                actual
        ));
    }

    private String generateStatLegendName(SpendJson spend) {
        return spend.category().name() + " " + spend.amount() + " " + "₽";
    }
}
