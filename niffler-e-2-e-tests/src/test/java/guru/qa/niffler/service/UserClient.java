package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.auth.AuthUserJson;

public interface UserClient {
    UserJson create(AuthUserJson user);
}
