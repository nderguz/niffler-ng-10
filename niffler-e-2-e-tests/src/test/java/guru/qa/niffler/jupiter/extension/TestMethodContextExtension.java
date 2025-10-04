package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestMethodContextExtension implements BeforeEachCallback, AfterEachCallback {

    private static final ThreadLocal<ExtensionContext> contextStore = new ThreadLocal<>();

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        contextStore.set(context);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        contextStore.remove();
    }

    public static ExtensionContext context(){
        return contextStore.get();
    }
}
