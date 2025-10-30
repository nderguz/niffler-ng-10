package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import jakarta.persistence.EntityNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, full_name, photo, photo_small)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().toString());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setString(5, user.getFullname());
            ps.setObject(6, user.getPhoto());
            ps.setObject(7, user.getPhotoSmall());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            user.setId(generatedKey);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.of(createUserEntity(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?)");
             PreparedStatement checkReverseInvitation = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                     "SELECT * FROM friendship WHERE requester_id = ? AND addressee_id = ?");
        ) {
            checkReverseInvitation.setObject(1, addressee.getId());
            checkReverseInvitation.setObject(2, requester.getId());
            checkReverseInvitation.execute();
            try (ResultSet rs = checkReverseInvitation.getResultSet()) {
                if (rs.next()) {
                    throw new SQLException("Already have income invitation from this user");
                }
            }
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.PENDING.name());
            ps.setDate(4, Date.valueOf(LocalDate.now()));

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?)");
             PreparedStatement checkReverseInvitation = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                     "SELECT * FROM friendship WHERE requester_id = ? AND addressee_id = ?")
        ) {
            checkReverseInvitation.setObject(1, addressee.getId());
            checkReverseInvitation.setObject(2, requester.getId());
            checkReverseInvitation.execute();
            try (ResultSet rs = checkReverseInvitation.getResultSet()) {
                if (rs.next()) {
                    throw new SQLException("Already have income invitation from this user");
                }
            }
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.PENDING.name());
            ps.setDate(4, Date.valueOf(LocalDate.now()));

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM friendship WHERE requester_id = ? AND addressee_id = ? and status = ?");
             PreparedStatement addFriendPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                     "UPDATE friendship SET status = ? WHERE requester_id = ? AND addressee_id = ?");
        ) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.PENDING.name());
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    addFriendPs.setString(1, FriendshipStatus.ACCEPTED.name());
                    addFriendPs.setObject(2, requester.getId());
                    addFriendPs.setObject(3, addressee.getId());
                    addFriendPs.executeUpdate();
                } else {
                    throw new EntityNotFoundException("Friendship request not found, requester=%s, addressee=%s".formatted(requester.getId(), addressee.getId()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserEntity createUserEntity(ResultSet rs) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getObject("id", UUID.class));
        user.setUsername(rs.getString("username"));
        user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        user.setFirstname(rs.getString("firstname"));
        user.setSurname(rs.getString("surname"));
        user.setFullname(rs.getString("full_name"));
        user.setPhoto(rs.getBytes("photo"));
        user.setPhotoSmall(rs.getBytes("photo_small"));
        return user;
    }
}
