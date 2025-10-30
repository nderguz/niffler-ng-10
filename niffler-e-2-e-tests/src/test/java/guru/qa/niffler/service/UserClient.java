package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

public interface UserClient {
    UserJson create(UserJson user);
    void addIncomeInvitation(UserJson requester, UserJson addressee);
    void addOutcomeInvitation(UserJson requester, UserJson addressee);
    void addFriend(UserJson requester, UserJson addressee);
}
