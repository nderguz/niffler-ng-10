package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.model.allure.ScreenDiff;
import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;

public class ScreenShootExtension implements BeforeEachCallback, ParameterResolver, TestExecutionExceptionHandler {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ScreenShootExtension.class);
    public static final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ScreenShotTest.class)
                .ifPresent(anno -> {
                    context.getStore(NAMESPACE)
                                    .put(context.getUniqueId(), anno.value());
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), ScreenShotTest.class) &&
                parameterContext.getParameter().getType().isAssignableFrom(BufferedImage.class);
    }

    @SneakyThrows
    @Override
    public BufferedImage resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final ExtensionContext methodContext = context();
        return ImageIO.read(new ClassPathResource(methodContext.getStore(NAMESPACE).get(methodContext.getUniqueId(), String.class)).getInputStream());
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        ScreenDiff screenDiff = new ScreenDiff(
                "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(getExpected())),
                "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(getActual())),
                "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(getDiff()))
        );

        Allure.addAttachment(
                "Screenshot diff",
                "application/vnd.allure.image.diff",
                objectMapper.writeValueAsString(screenDiff)
        );
        throw throwable;
    }

    public static void setExpected(BufferedImage expected){
        context().getStore(NAMESPACE).put("expected", expected);
    }

    public static BufferedImage getExpected(){
        return context().getStore(NAMESPACE).get("expected", BufferedImage.class);
    }

    public static void setActual(BufferedImage actual){
        context().getStore(NAMESPACE).put("actual", actual);
    }

    public static BufferedImage getActual(){
        return context().getStore(NAMESPACE).get("actual", BufferedImage.class);
    }

    public static void setDiff(BufferedImage diff){
        context().getStore(NAMESPACE).put("diff", diff);
    }

    public static BufferedImage getDiff(){
        return context().getStore(NAMESPACE).get("diff", BufferedImage.class);
    }

    private static byte[] imageToBytes(BufferedImage image){
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
