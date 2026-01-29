package guru.qa.niffler.model;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.user.UserJson;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public record TestData(
        String password,
        List<UserJson> incomeInvitations,
        List<UserJson> outcomeInvitations,
        List<UserJson> friends,
        List<CategoryJson> categories,
        List<SpendJson> spendings,
        List<UserJson> commonUsers
) {
    public TestData(String password) {
        this(password, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public TestData(String password, List<UserJson> friends, List<UserJson> incomeInvitations, List<UserJson> outcomeInvitations) {
        this(password, friends, incomeInvitations, outcomeInvitations, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public TestData(String password, List<UserJson> friends, List<UserJson> incomeInvitations, List<UserJson> outcomeInvitations, List<UserJson> commonUsers) {
        this(password, friends, incomeInvitations, outcomeInvitations, new ArrayList<>(), new ArrayList<>(), commonUsers);
    }
}