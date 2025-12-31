package guru.qa.niffler.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date createDateForSpending() {
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        formatter.format(today);
        return today;
    }
}
