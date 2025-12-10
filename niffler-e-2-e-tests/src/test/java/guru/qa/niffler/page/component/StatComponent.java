package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.Color;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.StatConditions.color;

public class StatComponent extends BaseComponent<StatComponent>{
    public StatComponent() {
        super($("#stat"));
    }

    private final ElementsCollection bubbles = self.$("#legend-container").$$("li");
    private final SelenideElement chart = $("canvas[role='img']");

    @Nonnull
    public BufferedImage chartScreenshot() throws IOException {
        return ImageIO.read($("canvas[role='img']").screenshot());
    }

    @Nonnull
    public StatComponent checkBubbles(Color... expectedColor){
        bubbles.should(color(expectedColor));
        return this;
    }
}
