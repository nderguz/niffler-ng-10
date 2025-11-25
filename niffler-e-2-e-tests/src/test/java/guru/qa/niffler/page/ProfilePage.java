package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class ProfilePage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement categoryInput = $("#category");
    private final SelenideElement archiveSwitcher = $(".MuiSwitch-input");
    private final ElementsCollection categoryCommon = $$(".MuiChip-filled.MuiChip-colorPrimary");
    private final ElementsCollection categoryArchived = $$(".MuiChip-filled.MuiChip-colorDefault");
    private final SelenideElement saveChangesBtn = $("button[type='submit']");
    private final SelenideElement uploadNewPictureInput = $("input[type='file']");
    private final SelenideElement registerPasskeyBtn = $("");

    public ProfilePage setNewName(String name) {
        nameInput.clear();
        nameInput.val(name);
        return this;
    }

    public ProfilePage uploadNewPicture(String path) {
        uploadNewPictureInput.uploadFromClasspath(path);
        return this;
    }

    public ProfilePage addCategory(String category) {
        categoryInput.setValue(category).pressEnter();
        return this;
    }

    public ProfilePage checkUsernameDisabled(){
        usernameInput.shouldBe(disabled);
        return this;
    }

    public ProfilePage saveChanges() {
        saveChangesBtn.click();
        return this;
    }

    public ProfilePage checkCategoryExists(String category) {
        categoryCommon.find(text(category)).shouldBe(visible);
        return this;
    }

    public ProfilePage checkArchivedCategoryExists(String category) {
        switchShowArchived();
        categoryArchived.find(text(category)).shouldBe(visible);
        return this;
    }

    public ProfilePage checkArchivedCategoryIsNotExists(String category){
        categoryArchived.find(text(category)).shouldBe(not(exist));
        return this;
    }

    public ProfilePage switchShowArchived(){
        archiveSwitcher.click();
        return this;
    }
}