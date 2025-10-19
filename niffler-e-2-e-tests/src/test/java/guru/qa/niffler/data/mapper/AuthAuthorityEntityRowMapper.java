package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthAuthorityEntityRowMapper implements RowMapper<AuthorityEntity> {

    public static final AuthAuthorityEntityRowMapper INSTANCE = new AuthAuthorityEntityRowMapper();

    private AuthAuthorityEntityRowMapper() {
    }

    @Override
    public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorityEntity ae = new AuthorityEntity();
        ae.setId(rs.getObject("id", UUID.class));
        ae.setAuthority(Authority.valueOf(rs.getString("authority")));
        ae.setUserId(rs.getObject("user_id", UUID.class));
        return ae;
    }
}
