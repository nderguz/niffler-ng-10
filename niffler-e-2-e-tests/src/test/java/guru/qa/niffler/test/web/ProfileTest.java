package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.spend.CategoryJson;
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
    @User(
            categories = @Category(archived = true)
    )
    @DisplayName("Архивная категория должна отображаться в списке категорий")
    public void checkArchivedCategoryExists(CategoryJson category) {
        page.checkArchivedCategoryIsNotExists(category.name());
    }

    @Test
//    @User(
//            username = "benito.rempel",
//            categories = @Category(
//                    archived = true
//            )
//    )
    @DisplayName("Активная категория должна отображаться в списке категорий")
    public void activeCategoryShouldPresentInCategoriesList() {
//        System.out.println(user);
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin("benito.rempel", "12345")
                .checkThatPageLoaded();
//                .openProfilePage()
//                .checkArchivedCategoryExists(user.testData().categories().getFirst().name());
    }

    @Test
    @User(
            categories = @Category(archived = true)
    )
    @DisplayName("Архивная категория не должна отображаться в списке категорий")
    public void archivedCategoryShouldNotPresentInCategoriesList(CategoryJson category) {
        page.checkArchivedCategoryIsNotExists(category.name());
    }
}

