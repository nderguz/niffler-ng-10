package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserDaoJdbc implements AuthUserDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\"  (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                        "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Created user not found in Result Set");
                }
                user.setId(generatedKey);
                return user;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" u JOIN authority a ON u.id = a.user_id WHERE u.id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                AuthUserEntity user = null;
                List<AuthorityEntity> authorityEntities = new ArrayList<>();
                while (rs.next()) {
                    if (user == null) {
                        user = AuthUserEntityRowMapper.INSTANCE.mapRow(rs, 1);
                    }

                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(user);
                    ae.setId(rs.getObject("a.id", UUID.class));
                    ae.setAuthority(Authority.valueOf(rs.getString("authority")));
                    authorityEntities.add(ae);

                    AuthUserEntity au = new AuthUserEntity();
                    au.setId(rs.getObject("id", UUID.class));
                    au.setUsername(rs.getString("username"));
                    au.setPassword(rs.getString("password"));
                    au.setEnabled(rs.getBoolean("enabled"));
                    au.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    au.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    au.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setAuthorities(authorityEntities);
                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE from \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, UUID.class);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" "
        )) {
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                List<AuthUserEntity> entities = new ArrayList<>();
                while (rs.next()) {
                    AuthUserEntity au = new AuthUserEntity();
                    au.setId(rs.getObject("id", UUID.class));
                    au.setUsername(rs.getString("username"));
                    au.setPassword(rs.getString("password"));
                    au.setEnabled(rs.getBoolean("enabled"));
                    au.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    au.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    au.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    entities.add(au);
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
