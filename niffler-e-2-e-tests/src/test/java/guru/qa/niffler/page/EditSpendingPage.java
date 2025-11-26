package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage {
  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement saveBtn = $("#save");
    private final Header header = new Header();

    public @Nonnull Header getHeader(){
        return header;
    }

  @Step("Ввести новое описание траты: {description}")
  public @Nonnull EditSpendingPage setNewSpendingDescription(String description) {
    descriptionInput.val(description);
    return this;
  }

  @Step("Нажать на кнопку \"Save\"")
  public @Nonnull MainPage save() {
    saveBtn.click();
    return new MainPage();
  }
}