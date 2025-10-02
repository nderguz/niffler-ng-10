package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.UserType;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.jupiter.annotation.UserType.Type;


public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(
            String username,
            String password,
            String friend,
            String income,
            String outcome
    ) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("test1", "test1", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("test2", "test2", "test5", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("test3", "test3", null, "test4", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("test4", "test4", null, null, "test3"));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("test5", "test5", null, null, "test6"));
    }


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        List<Type> userTypes = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> p.getAnnotation(UserType.class).value())
                .toList();
        if (userTypes.isEmpty()) {
            return;
        }

        Map<Type, List<StaticUser>> users = new HashMap<>();
        for (Type userType : userTypes) {
            Optional<StaticUser> user = Optional.empty();
            StopWatch sw = StopWatch.createStarted();
            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                switch (userType) {
                    case EMPTY -> user = Optional.ofNullable(EMPTY_USERS.poll());
                    case WITH_FRIEND -> user = Optional.ofNullable(WITH_FRIEND_USERS.poll());
                    case WITH_INCOME_REQUEST -> user = Optional.ofNullable(WITH_INCOME_REQUEST_USERS.poll());
                    case WITH_OUTCOME_REQUEST -> user = Optional.ofNullable(WITH_OUTCOME_REQUEST_USERS.poll());
                }
            }
            user.ifPresentOrElse(u -> {
                users.computeIfAbsent(userType, k -> new ArrayList<>()).add(u);
            }, () -> new IllegalStateException("Cant find user after 30 sec"));
        }
        context.getStore(NAMESPACE).put(context.getUniqueId(), users);
        Allure.getLifecycle().updateTestCase(testCase -> testCase.setStart(new Date().getTime()));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<Type, List<StaticUser>> users = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        for (Map.Entry<Type, List<StaticUser>> entry : users.entrySet()) {
            switch (entry.getKey()) {
                case EMPTY -> EMPTY_USERS.addAll(entry.getValue());
                case WITH_FRIEND -> WITH_FRIEND_USERS.addAll(entry.getValue());
                case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS.addAll(entry.getValue());
                case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS.addAll(entry.getValue());
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        List<StaticUser> test = (List<StaticUser>) extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class)
                .get(parameterContext.getParameter().getAnnotation(UserType.class).value());
        return test.stream().findFirst().get();
    }
}
