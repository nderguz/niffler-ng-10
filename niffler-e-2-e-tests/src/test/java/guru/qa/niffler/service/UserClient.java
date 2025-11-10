package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

public interface UserClient {
    UserJson create(UserJson user);
    void addInvitation(UserJson requester, UserJson addressee);
    void addFriend(UserJson requester, UserJson addressee);
}
