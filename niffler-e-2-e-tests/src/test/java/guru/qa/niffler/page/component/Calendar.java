package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Month;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.util.Calendar.*;

@ParametersAreNonnullByDefault
public class Calendar {

    private final SelenideElement calendarButton = $("button[aria-label*='Choose date']");
    private final SelenideElement previousMonthBtn = $("button[title='Previous month']");
    private final SelenideElement nextMonthBtn = $("button[title='Next month']");
    private final SelenideElement currentDate = $(".MuiPickersCalendarHeader-label");
    private final ElementsCollection dateRows = $$(".MuiDayCalendar-weekContainer");

    @Step("Выбор даты в календаре: {date}")
    public @Nonnull Calendar selectDateInCalendar(Date date) {
        java.util.Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendarButton.click();

        selectYear(calendar.get(YEAR));
        selectMonth(calendar.get(MONTH));
        selectDay(calendar.get(DAY_OF_MONTH));
        return this;
    }

    private void selectYear(int year) {
        var actualYearIndex = Integer.parseInt(getDateFromComponent()[1]);

        while (actualYearIndex > year){
            previousMonthBtn.click();
            actualYearIndex = Integer.parseInt(getDateFromComponent()[1]);
        }

        while ((actualYearIndex < year)){
            nextMonthBtn.click();
            actualYearIndex = Integer.parseInt(getDateFromComponent()[1]);
        }
    }

    private void selectMonth(int month) {
        var actualMonthIndex = Month.valueOf(getDateFromComponent()[0]
                        .toUpperCase())
                .ordinal();

        while (actualMonthIndex > month){
            previousMonthBtn.click();
            actualMonthIndex = Month.valueOf(getDateFromComponent()[0]
                            .toUpperCase())
                    .ordinal();
        }

        while(actualMonthIndex < month){
            nextMonthBtn.click();
            actualMonthIndex = Month.valueOf(getDateFromComponent()[0]
                            .toUpperCase())
                    .ordinal();
        }
    }

    private void selectDay(int day) {
        var rows = dateRows.snapshot();
        for (SelenideElement row : rows) {
            var days = row.$$("button").snapshot();
            for (SelenideElement d : days) {
                if (d.getText().equals(String.valueOf(day))) {
                    d.click();
                }
            }
        }
    }

    private @Nonnull String[] getDateFromComponent(){
        return currentDate.should(matchText(".*\\d{4}"))
                .getText()
                .split(" ");
    }
}