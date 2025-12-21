package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.impl.UserApiClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class UserExtension implements BeforeEachCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    public static final String DEFAULT_PASSWORD = "12345";

    private final UserClient usersClient = new UserApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                            if ("".equals(userAnno.username())) {
                                final String username = randomUsername();
                                final UserJson user = usersClient.create(username, DEFAULT_PASSWORD);
                                final List<UserJson> incomeInvitations = usersClient.addIncomeInvitation(user, userAnno.incomeInvitations());
                                final List<UserJson> outcomeInvitations = usersClient.addOutcomeInvitation(user, userAnno.outcomeInvitations());
                                final List<UserJson> friends = usersClient.addFriend(user, userAnno.friends());

                                final TestData testData = new TestData(
                                        DEFAULT_PASSWORD,
                                        incomeInvitations,
                                        outcomeInvitations,
                                        friends,
                                        new ArrayList<>(),
                                        new ArrayList<>()
                                );
                                System.out.println("UE user data = " + user);
                                System.out.println("UE test data = " + testData);
                                user.addTestData(testData);
                                setUser(user);
                            }
                        }
                );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return getUser().orElseThrow();
    }

    public static void setUser(UserJson user) {
        final ExtensionContext context = context();
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                user
        );
    }

    public static Optional<UserJson> getUser() {
        final ExtensionContext context = context();
        return Optional.ofNullable(context.getStore(NAMESPACE)
                .get(context.getUniqueId(), UserJson.class));
    }
}