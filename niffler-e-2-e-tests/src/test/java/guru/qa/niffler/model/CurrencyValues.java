package guru.qa.niffler.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CurrencyValues {
  RUB("₽"), USD("$"), EUR("€"), KZT("₸");

  private final String sing;
}
