package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.service.impl.SpendApiClient;
import guru.qa.niffler.service.impl.UserApiClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

import java.util.ArrayList;
import java.util.List;

public class ApiLoginExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);
    private static final Config CFG = Config.getInstance();
    private final boolean setupBrowser;

    private final AuthApiClient authApiClient = new AuthApiClient();
    private final UserClient usersClient = new UserApiClient();
    private final SpendClient spendClient = new SpendApiClient();

    private ApiLoginExtension(boolean setupBrowser) {
        this.setupBrowser = setupBrowser;
    }
    public ApiLoginExtension() {
        this.setupBrowser = true;
    }

    public static ApiLoginExtension restApiLoginExtension(){
        return new ApiLoginExtension(false);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class)
                .ifPresent(apiLogin -> {
                    final UserJson userToLogin;
                    final var userFromUserExtension = UserExtension.getUser();
                    if ("".equals(apiLogin.username()) || "".equals(apiLogin.password())) {
                        if(userFromUserExtension.isEmpty()){
                            throw new IllegalStateException("@User must be present in case that @ApiLogin is empty");
                        }
                        userToLogin = userFromUserExtension.get();
                    }else{
                        var username = apiLogin.username();
                        final List<CategoryJson> categories = spendClient.getCategories(username, false);
                        final List<SpendJson> spends = spendClient.getSpends(username);

                        final List<UserJson> friendsList = usersClient.getFriends(username, null);
                        final List<UserJson> friends = friendsList.stream()
                                .filter(f -> f.getFriendshipStatus() != null && f.getFriendshipStatus().equals(FriendshipStatus.FRIEND))
                                .toList();
                        final List<UserJson> incomeInvitations = friendsList.stream()
                                .filter(f -> f.getFriendshipStatus() != null &&  f.getFriendshipStatus().equals(FriendshipStatus.INVITE_RECEIVED))
                                .toList();

                        final List<UserJson> outcomeInvitations = usersClient.allUsers(username, null).stream()
                                .filter(f -> f.getFriendshipStatus() != null && f.getFriendshipStatus().equals(FriendshipStatus.INVITE_SENT))
                                .toList();

                        var testData = new TestData(
                                apiLogin.password(),
                                incomeInvitations,
                                outcomeInvitations,
                                friends,
                                categories,
                                spends,
                                new ArrayList<>()
                        );
                        var fakeUser = new UserJson(
                                apiLogin.username(),
                                testData
                        );
                        if (userFromUserExtension.isPresent()){
                            throw new IllegalStateException("@User must not be present in case that @ApiLogin contains username or password");
                        }
                        UserExtension.setUser(fakeUser);
                        userToLogin = fakeUser;
                    }

                    final String token = authApiClient.loginUser(userToLogin.getUsername(), userToLogin.getTestData().password());
                    setToken(token);
                    if(setupBrowser){
                        Selenide.open(CFG.frontUrl());
                        Selenide.localStorage().setItem("id_token", getToken());
                        WebDriverRunner.getWebDriver().manage().addCookie(
                                new Cookie(
                                        "JSESSIONID",
                                        ThreadSafeCookieStore.INSTANCE.getCookieByName("JSESSIONID")
                                )
                        );
                        Selenide.open(MainPage.URL, MainPage.class).checkThatPageLoaded();
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(String.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), Token.class);
    }

    @Override
    public String resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return "Bearer " + getToken();
    }

    public static void setToken(String token) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("token", token);
    }

    public static String getToken() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("token", String.class);
    }

    public static void setCode(String code) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("code", code);
    }

    public static String getCode() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("code", String.class);
    }

    public static Cookie getJsessionIdCookie() {
        return new Cookie(
                "JSESSIONID",
                ThreadSafeCookieStore.INSTANCE.getCookieByName("JSESSIONID")
        );
    }
}
