package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

public interface UserClient {
    UserJson create(String username, String password);
    void createInvitation(UserJson targetUser, int count);
    void addInvitation(UserJson requester, UserJson addressee);
    void addFriend(UserJson requester, UserJson addressee);
}
