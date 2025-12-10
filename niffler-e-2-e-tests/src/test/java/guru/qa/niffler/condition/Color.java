package guru.qa.niffler.condition;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Color {
    yellow("255, 183, 3, 1"),
    green("53, 173, 123, 1");

    public final String rgb;
}
