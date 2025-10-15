package guru.qa.niffler.model.auth;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AuthUserJson {

    private UUID id;
    private String username;
    private String password;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    public static AuthUserJson fromEntity(AuthUserEntity entity){
        AuthUserJson au = new AuthUserJson();
        au.setId(entity.getId());
        au.setUsername(entity.getUsername());
        au.setPassword(entity.getPassword());
        au.setEnabled(entity.getEnabled());
        au.setAccountNonExpired(entity.getAccountNonExpired());
        au.setAccountNonLocked(entity.getAccountNonLocked());
        au.setCredentialsNonExpired(entity.getCredentialsNonExpired());
        return au;
    }
}
