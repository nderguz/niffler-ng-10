package guru.qa.niffler.data.repository.impl.springjdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();
    private final UserdataUserDao userDao = new UdUserDaoSpringJdbc();

    @Override
    public UserEntity create(UserEntity user) {
        return userDao.create(user);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userDao.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public UserEntity update(UserEntity user) {
        return userDao.update(user);
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
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

    @Override
    public void remove(UserEntity user) {
        userDao.delete(user);
    }
}
