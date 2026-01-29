package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class RandomDataUtils {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    public static String randomUsername(){
        return faker.name().username();
    }

    public static String randomName(){
        return faker.name().name();
    }

    public static String randomPassword(){
        return faker.name().lastName();
    }

    public static String randomCategoryName(){
        return faker.name().title();
    }

    public static SpendJson randomSpend(String username){
        return new SpendJson(
                null,
                null,
                new CategoryJson(
                       null,
                        faker.name().title(),
                        username,
                        false
                ),
                null,
                faker.number().randomDouble(1000, 1, 1000),
                faker.name().title(),
                username
        );
    }

    public static double nextDouble(int scale){
        return BigDecimal.valueOf(random.nextDouble())
                .setScale(scale, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static String randomSentence(int wordsCount){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i <= wordsCount; i++){
            sb.append(faker.name().name());
            sb.append(" ");
        }
        return sb.toString();
    }
}
