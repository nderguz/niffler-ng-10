package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.condition.model.Bubble;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.util.*;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

public class StatConditions {
    public static WebElementCondition statBubble(Color expectedColor) {
        return new WebElementCondition("color") {
            @Nonnull
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                final String rgba = element.getCssValue("background-color");
                return new CheckResult(
                        expectedColor.rgb.equals(rgba),
                        rgba
                );
            }
        };
    }

    public static WebElementsCondition statBubble(Bubble... expectedColors) {
        return new WebElementsCondition() {
            private final String expected = Arrays.stream(expectedColors).map(c -> c.color().rgb + "=" + c.text()).toList().toString();
            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedColors)) {
                    throw new IllegalArgumentException("No expected colors given");
                }

                if (expectedColors.length != elements.size()) {
                    String message = String.format("List size mismatch (expected: %s, actual: %s", expectedColors.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                Map<String, String> actualRgbaList = new HashMap<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);

                    final String colorToCheck = expectedColors[i].color().rgb;
                    final String textToCheck = expectedColors[i].text();
                    final String textElement = elementToCheck.getText();
                    final String rgbaElement = elementToCheck.getCssValue("background-color");
                    actualRgbaList.put(rgbaElement, textElement);
                    if (passed) {
                        passed = rgbaElement.equals(colorToCheck) && textToCheck.equals(textElement);
                    }
                }
                if (!passed) {
                    final String actualRgba = actualRgbaList.toString();
                    final String message = String.format("List colors mismatch (expected: %s, actual: %s", expected, actualRgba);
                    return rejected(message, actualRgba);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expected;
            }
        };
    }

    public static WebElementsCondition statBubblesInAnyOrder(Bubble... expectedColors) {
        return new WebElementsCondition() {
            private final String expected = Arrays.stream(expectedColors).map(c -> c.color().rgb + "=" + c.text()).toList().toString();
            @Override
            @Nonnull
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedColors)) {
                    throw new IllegalArgumentException("No expected colors given");
                }

                if (expectedColors.length != elements.size()) {
                    String message = String.format("List size mismatch (expected: %s, actual: %s", expectedColors.length, elements.size());
                    return rejected(message, elements);
                }

                Map<String, String> actualRgbaListWithText = new HashMap<>();
                for (final WebElement elementToCheck : elements) {
                    final String textElement = elementToCheck.getText();
                    final String rgbaElement = elementToCheck.getCssValue("background-color");
                    actualRgbaListWithText.put(rgbaElement, textElement);
                }
                boolean passed = true;
                for (int i = 0; i < elements.size(); i++) {
                    final String colorToCheck = expectedColors[i].color().rgb;
                    final String textToCheck = expectedColors[i].text();
                    var exsisted = actualRgbaListWithText.get(colorToCheck);
                    if (passed) {
                        passed = exsisted.equals(textToCheck);
                    }
                }
                if (!passed) {
                    final String actualRgba = actualRgbaListWithText.toString();
                    final String message = String.format("List colors mismatch (expected: %s, actual: %s", expected, actualRgba);
                    return rejected(message, actualRgba);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expected;
            }
        };
    }

    public static WebElementsCondition statBubblesContains(Bubble... expectedColors) {
        return new WebElementsCondition() {
            private final String expected = Arrays.stream(expectedColors).map(c -> c.color().rgb + "=" + c.text()).toList().toString();
            @Override
            @Nonnull
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedColors)) {
                    throw new IllegalArgumentException("No expected colors given");
                }

                Map<String, String> actualRgbaListWithText = new HashMap<>();
                for (final WebElement elementToCheck : elements) {
                    final String textElement = elementToCheck.getText();
                    final String rgbaElement = elementToCheck.getCssValue("background-color");
                    actualRgbaListWithText.put(rgbaElement, textElement);
                }
                boolean passed = true;
                for (int i = 0; i < expectedColors.length; i++) {
                    final String colorToCheck = expectedColors[i].color().rgb;
                    final String textToCheck = expectedColors[i].text();
                    var exsisted = actualRgbaListWithText.get(colorToCheck);
                    if (passed) {
                        passed = exsisted.equals(textToCheck);
                    }
                }
                if (!passed) {
                    final String actualRgba = actualRgbaListWithText.toString();
                    final String message = String.format("List colors mismatch (expected: %s, actual: %s", expected, actualRgba);
                    return rejected(message, actualRgba);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expected;
            }
        };
    }
}
