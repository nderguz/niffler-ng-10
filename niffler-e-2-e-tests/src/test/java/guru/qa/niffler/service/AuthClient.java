package guru.qa.niffler.service;

import guru.qa.niffler.model.auth.AuthUserJson;

public interface AuthClient {

    AuthUserJson createWithError(AuthUserJson user);

    AuthUserJson create(AuthUserJson user);

    void delete(AuthUserJson user);
}
