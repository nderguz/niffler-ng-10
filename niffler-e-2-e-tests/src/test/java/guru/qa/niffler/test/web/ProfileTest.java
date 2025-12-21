package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.open;

@DisplayName("Профиль пользователя")
public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @User(
            categories = @Category(archived = true)
    )
    @DisplayName("Архивная категория должна отображаться в списке категорий")
    public void checkArchivedCategoryExists(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .checkArchivedCategoryIsNotExists(user.getTestData().categories().getFirst().name());
    }

    @Test
    @User(
            categories = @Category(
                    archived = true
            )
    )
    @DisplayName("Активная категория должна отображаться в списке категорий")
    public void activeCategoryShouldPresentInCategoriesList(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .checkArchivedCategoryExists(user.getTestData().categories().getFirst().name());
    }

    @Test
    @User(
            categories = @Category(archived = true)
    )
    @DisplayName("Архивная категория не должна отображаться в списке категорий")
    public void archivedCategoryShouldNotPresentInCategoriesList(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .checkArchivedCategoryIsNotExists(user.getTestData().categories().getFirst().name());
    }

    @User
    @ScreenShotTest("img/profile-pic.png")
    public void uploadNewProfilePictureShouldBeVisible(UserJson user, BufferedImage expected) throws IOException {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.getUsername(), user.getTestData().password())
                .getHeader()
                .toProfilePage()
                .uploadNewPicture("img/profile-pic.png")
                .assertProfilePicScreenshot(expected);
    }
}

