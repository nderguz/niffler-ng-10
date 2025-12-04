package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class Popup extends BaseComponent<Popup> {

    public Popup() {
        super($("div[role='dialog']"));
    }

    public void clickBtnByText(String text) {
        self.find(byText(text)).click();
    }
}
