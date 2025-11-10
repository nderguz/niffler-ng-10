package guru.qa.niffler.data.repository.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserRepositoryJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();
    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        try (PreparedStatement userPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\"  (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                        "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement authorityPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                     "INSERT INTO authority (user_id, authority) " +
                             "VALUES (?, ?)")) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getPassword());
            userPs.setBoolean(3, user.getEnabled());
            userPs.setBoolean(4, user.getAccountNonExpired());
            userPs.setBoolean(5, user.getAccountNonLocked());
            userPs.setBoolean(6, user.getCredentialsNonExpired());

            userPs.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = userPs.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Created user not found in Result Set");
                }
                user.setId(generatedKey);

                for (AuthorityEntity authority : user.getAuthorities()) {
                    authorityPs.setObject(1, generatedKey);
                    authorityPs.setString(2, authority.getAuthority().name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }
                authorityPs.executeBatch();
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        try (PreparedStatement userPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "UPDATE \"user\" SET username = ?, password = ?, enabled = ?, account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ? " +
                        "WHERE id = ?")) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getPassword());
            userPs.setBoolean(3, user.getEnabled());
            userPs.setBoolean(4, user.getAccountNonExpired());
            userPs.setBoolean(5, user.getAccountNonLocked());
            userPs.setBoolean(6, user.getCredentialsNonExpired());
            userPs.setObject(7, user.getId());

            userPs.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return authUserDao.findById(id);
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" u JOIN authority a ON u.id = a.user_id WHERE u.username = ?"
        )) {
            ps.setObject(1, username);
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
    public void remove(AuthUserEntity user) {
        authUserDao.deleteById(user.getId());
    }
}
