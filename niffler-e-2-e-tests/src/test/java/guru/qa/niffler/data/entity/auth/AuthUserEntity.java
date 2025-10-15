package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.auth.AuthUserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthUserEntity implements Serializable {
    private UUID id;

    private String username;

    private String password;

    private Boolean enabled;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;


    public static AuthUserEntity fromJson(AuthUserJson user){
        AuthUserEntity ue = new AuthUserEntity();

        if (user.getId() != null){
            ue.setId(user.getId());
        }

        ue.setUsername(user.getUsername());
        ue.setPassword(user.getPassword());
        ue.setEnabled(user.isEnabled());
        ue.setAccountNonExpired(user.isAccountNonExpired());
        ue.setAccountNonLocked(user.isAccountNonLocked());
        ue.setCredentialsNonExpired(user.isCredentialsNonExpired());

        return ue;
    }
}
