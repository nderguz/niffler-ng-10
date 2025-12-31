package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.spend.SpendJson;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

public class SpendConditions {

    public static WebElementsCondition spends(List<SpendJson> expectedSpends) {
        return new WebElementsCondition() {
            private final List<String> expected = mapToStringList(expectedSpends);
            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (expectedSpends.isEmpty()) {
                    throw new IllegalArgumentException("No expected spends given");
                }

                if (expectedSpends.size() != elements.size()) {
                    String message = String.format("List size mismatch (expected: %s, actual: %s", expectedSpends.size(), elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;

                List<String> actualResultText = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final String actualText = elements.get(i).getText();
                    actualResultText.add(actualText);

                }

                for (int i = 0; i < expected.size(); i++) {
                    var expectedText = expected.get(i);
                    var actualText = actualResultText
                            .stream()
                            .filter(t -> t.equals(expectedText))
                            .findFirst();
                    if(passed){
                        passed = actualText.isPresent();
                    }
                }
                if(!passed){
                    final String message = String.format("Spends mismatch (expected: %s, actual: %s", expected, actualResultText);
                    return rejected(message, actualResultText);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expected.toString();
            }
        };
    }

    @Nonnull
    private static List<String> mapToStringList(List<SpendJson> expectedSpends) {
        if (expectedSpends.isEmpty()) {
            throw new IllegalArgumentException("Expected spends list is empty");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);

        return expectedSpends.stream()
                .map(s -> s.category().name() + " " + parseDouble(s.amount())  + " " + s.currency().getSing() + " " + s.description() + " " + sdf.format(s.spendDate()))
                .toList();
    }

    @Nonnull
    private static String parseDouble(Double amount){
        DecimalFormat df = new DecimalFormat("#");
        if(Math.floor(amount) < amount || Math.rint(amount) > amount){
            return amount.toString();
        }else{
            return df.format(amount);
        }
    }
}