package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class AllPeoplePage extends BasePage<AllPeoplePage> {

    public static final String URL = CFG.frontUrl() + "people/all";
    private final SearchField searchField = new SearchField();
    private final SelenideElement allPeopleTable = $(".MuiTable-root");
    private final ElementsCollection allPeopleRows = allPeopleTable.$$("tbody tr");

    @Step("Поиск пользователя с никнеймом {username}")
    public @Nonnull AllPeoplePage searchPerson(String username) {
        searchField.search(username);
        return this;
    }

    @Step("Проверить исходящее приглашение пользователю {username}")
    public @Nonnull AllPeoplePage checkOutcomeInvitationShouldBeVisible(String username) {
        searchField.search(username);
        allPeopleRows.findBy(text(username))
                .shouldBe(visible)
                .$(".MuiChip-label")
                .shouldHave(text("Waiting..."));
        return this;
    }
}
