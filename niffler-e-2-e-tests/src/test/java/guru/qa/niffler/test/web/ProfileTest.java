package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@DisplayName("Профиль пользователя")
public class ProfileTest {

    private static final Config CFG = Config.getInstance();
    private ProfilePage page;

    @BeforeEach
    public void setUp() {
        page = open(CFG.frontUrl(), LoginPage.class)
                .successLogin("test", "test")
                .checkThatPageLoaded()
                .openProfilePage();
    }

    @Test
    @Category(username = "test", archived = true)
    @DisplayName("Архивная категория должна отображаться в списке категорий")
    public void checkArchivedCategoryExists(CategoryJson category) {
        page.checkArchivedCategoryIsNotExists(category.name());
    }

    @Test
    @Category(username = "test")
    @DisplayName("Активная категория должна отображаться в списке категорий")
    public void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        page.checkCategoryExists(category.name());
    }

    @Test
    @Category(username = "test", archived = true)
    @DisplayName("Архивная категория не должна отображаться в списке категорий")
    public void archivedCategoryShouldNotPresentInCategoriesList(CategoryJson category) {
        page.checkArchivedCategoryIsNotExists(category.name());
    }
}

