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
import java.util.List;

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

                List<String> actualResult = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    /* Только под конец понял, что прохожусь не по каждой ячейке, а вытаскиваю полный текст спендинга, который
                    в actualText сохраняется примерно как "Test category 1 1000 ₽ Test description 1 Dec 11, 2025", и формирую аналогичную строку
                    из SpendJson и сравниваю их.
                    В целом проверки работают, но если принципиально нужно проходиться по каждой ячейке и сравнивать с полем, могу переделать
                     */
                    final String actualText = elements.get(i).getText();
                    final String expectedText = expected.get(i);
                    actualResult.add(actualText);
                    if(passed){
                        passed = actualText.equals(expectedText);
                    }
                }
                if(!passed){
                    final String message = String.format("Spends mismatch (expected: %s, actual: %s", expected, actualResult);
                    return rejected(message, actualResult);
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
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");

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