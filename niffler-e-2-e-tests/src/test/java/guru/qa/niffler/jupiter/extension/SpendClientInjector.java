package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;


/**
 * Создание DI для SpendClient. Работает только с классами-тестами
 */
public class SpendClientInjector implements TestInstancePostProcessor {
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        for (Field declaredField : testInstance.getClass().getDeclaredFields()) {
            if(declaredField.getType().isAssignableFrom(SpendClient.class)) {
                declaredField.setAccessible(true);
                declaredField.set(testInstance, new SpendDbClient());
            }
        }

    }
}