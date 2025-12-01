package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class ProfilePage extends  BasePage<ProfilePage> {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement categoryInput = $("#category");
    private final SelenideElement archiveSwitcher = $(".MuiSwitch-input");
    private final ElementsCollection categoryCommon = $$(".MuiChip-filled.MuiChip-colorPrimary");
    private final ElementsCollection categoryArchived = $$(".MuiChip-filled.MuiChip-colorDefault");
    private final SelenideElement saveChangesBtn = $("button[type='submit']");
    private final SelenideElement uploadNewPictureInput = $("input[type='file']");
    private final SelenideElement registerPasskeyBtn = $("");

    @Step("Ввести новое имя пользователя: {name}")
    public @Nonnull ProfilePage setNewName(String name) {
        nameInput.clear();
        nameInput.val(name);
        return this;
    }

    @Step("Загрузить новую картинку профиля")
    public @Nonnull ProfilePage uploadNewPicture(String path) {
        uploadNewPictureInput.uploadFromClasspath(path);
        return this;
    }

    @Step("Добавить новую категорию")
    public @Nonnull ProfilePage addCategory(String category) {
        categoryInput.setValue(category).pressEnter();
        return this;
    }

    @Step("Проверить, что изменение юзернейма недоступно")
    public @Nonnull ProfilePage checkUsernameDisabled() {
        usernameInput.shouldBe(disabled);
        return this;
    }

    @Step("Нажать кнопку \"Сохранить изменения\"")
    public @Nonnull ProfilePage saveChanges() {
        saveChangesBtn.click();
        return this;
    }

    @Step("Проверить, что категория {category} существует")
    public @Nonnull ProfilePage checkCategoryExists(String category) {
        categoryCommon.find(text(category)).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что архивная категория {category} существует")
    public @Nonnull ProfilePage checkArchivedCategoryExists(String category) {
        switchShowArchived();
        categoryArchived.find(text(category)).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что архивная категория {category} не существует")
    public @Nonnull ProfilePage checkArchivedCategoryIsNotExists(String category) {
        categoryArchived.find(text(category)).shouldBe(not(exist));
        return this;
    }

    @Step("Нажать на переключатель \"Show archived\"")
    public @Nonnull ProfilePage switchShowArchived() {
        archiveSwitcher.click();
        return this;
    }

    @Step("Проверить поле Name")
    public ProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }
}