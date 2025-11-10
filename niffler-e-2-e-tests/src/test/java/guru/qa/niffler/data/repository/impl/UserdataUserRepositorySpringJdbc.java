package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.extractor.UserdataSetExtractor;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"user\" (username, currency, firstname, surname, full_name, photo, photo_small)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().toString());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setString(5, user.getFullname());
            ps.setObject(6, user.getPhoto());
            ps.setObject(7, user.getPhotoSmall());
            return ps;
        }, kh);

        final UUID generatedKey = (UUID) kh.getKeys().get("id" );
        user.setId(generatedKey);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        "SELECT * FROM \"user\" u " +
                                "JOIN friendship fr " +
                                "ON u.id = fr.addressee_id " +
                                "WHERE u.id = ?",
                        UserdataSetExtractor.INSTANCE,
                        id
                )
        );
    }

    @Override
    public void addInvitation(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO  friendship (requester_id, addressee_id, status) " +
                            "VALUES (?, ?, ?) " +
                            "ON CONFLICT (requester_id, addressee_id) " +
                            "DO UPDATE SET status = ?"
            );
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.PENDING.name());
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            return ps;
        });
    }


    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        jdbcTemplate.batchUpdate(
                "INSERT INTO  friendship (requester_id, addressee_id, status) " +
                        "VALUES (?, ?, ?) " +
                        "ON CONFLICT (requester_id, addressee_id) " +
                        "DO UPDATE SET status = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        if (i == 0) {
                            ps.setObject(1, requester.getId());
                            ps.setObject(2, addressee.getId());
                        } else {
                            ps.setObject(1, addressee.getId());
                            ps.setObject(2, requester.getId());
                        }
                        ps.setString(3, FriendshipStatus.ACCEPTED.name());
                        ps.setDate(4, Date.valueOf(LocalDate.now()));
                    }

                    @Override
                    public int getBatchSize() {
                        return 2;
                    }
                }
        );
    }
}
