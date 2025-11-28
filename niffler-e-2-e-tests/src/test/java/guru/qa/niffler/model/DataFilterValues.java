package guru.qa.niffler.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DataFilterValues {

    ALL("All"),
    MONTH("Month"),
    WEEK("Week"),
    TODAY("Today");

    private final String period;
}