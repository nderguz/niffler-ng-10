package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.open;

@DisplayName("Профиль пользователя")
@WebTest
public class ProfileTest {

    @Test
    @User(
            categories = @Category(archived = true)
    )
    @ApiLogin
    @DisplayName("Архивная категория должна отображаться в списке категорий")
    public void checkArchivedCategoryExists(UserJson user) {
        open(ProfilePage.URL, ProfilePage.class)
                .checkArchivedCategoryIsNotExists(user.getTestData().categories().getFirst().name());
    }

    @Test
    @User(
            categories = @Category(
                    archived = true
            )
    )
    @ApiLogin
    @DisplayName("Активная категория должна отображаться в списке категорий")
    public void activeCategoryShouldPresentInCategoriesList(UserJson user) {
        open(ProfilePage.URL, ProfilePage.class)
                .checkArchivedCategoryExists(user.getTestData().categories().getFirst().name());
    }

    @Test
    @User(
            categories = @Category(archived = true)
    )
    @ApiLogin
    @DisplayName("Архивная категория не должна отображаться в списке категорий")
    public void archivedCategoryShouldNotPresentInCategoriesList(UserJson user) {
        open(ProfilePage.URL, ProfilePage.class)
                .checkArchivedCategoryIsNotExists(user.getTestData().categories().getFirst().name());
    }

    @User
    @ApiLogin
    @ScreenShotTest(value = "img/profile-pic.png", rewriteExpected = true)
    //todo refactor
    public void uploadNewProfilePictureShouldBeVisible(UserJson user, BufferedImage expected) throws IOException {
        open(ProfilePage.URL, ProfilePage.class)
                .uploadNewPicture("img/profile-pic.png")
                .assertProfilePicScreenshot(expected);
    }
}

