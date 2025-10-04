package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

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

    public static String randomSentence(int wordsCount){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i <= wordsCount; i++){
            sb.append(faker.name().name());
            sb.append(" ");
        }
        return sb.toString();
    }
}
