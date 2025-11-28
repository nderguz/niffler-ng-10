package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class Header {

    private final SelenideElement self = $("#root header");
    private final SelenideElement menuBtn = self.$("button[aria-label='Menu']");
    private final ElementsCollection menuOptions = $$("li a");
    private final SelenideElement mainIcon = self.$("h1");
    private final SelenideElement newSpendingBtn = self.$("a[href='/spending']");

    @Step("Открыть страницу со списком друзей")
    public @Nonnull FriendsPage toFriendsPage() {
        menuBtn.click();
        menuOptions.find(text("Friends")).click();
        return new FriendsPage();
    }

    @Step("Открыть страницу со всеми пользователями")
    public @Nonnull AllPeoplePage toAllPeoplesPage() {
        menuBtn.click();
        menuOptions.find(text("All People")).click();
        return new AllPeoplePage();
    }

    @Step("Открыть профиль пользователя")
    public @Nonnull ProfilePage toProfilePage() {
        menuBtn.click();
        menuOptions.find(text("Profile")).click();
        return new ProfilePage();
    }

    @Step("Выйти из профиля")
    public @Nonnull LoginPage signOut() {
        menuBtn.click();
        menuOptions.find(text("Sign out")).click();
        return new LoginPage();
    }

    @Step("Переход на страницу добавления новой траты")
    public @Nonnull EditSpendingPage toAddSpendingPage() {
        newSpendingBtn.click();
        return new EditSpendingPage();
    }

    @Step("Перейти на главную страницу")
    public @Nonnull MainPage toMainPage() {
        mainIcon.click();
        return new MainPage();
    }
}
