package guru.qa.niffler.data.extractor;

import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.data.entity.user.FriendshipEntity;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserdataSetExtractor implements ResultSetExtractor<UserEntity> {

    public static UserdataSetExtractor INSTANCE = new UserdataSetExtractor();

    private UserdataSetExtractor() {
    }

    @Override
    public UserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, UserEntity> users = new ConcurrentHashMap<>();
        UserEntity user = null;
        while (rs.next()) {
            UUID userId = rs.getObject("id", UUID.class);

            user = users.computeIfAbsent(userId, id -> {
                UserEntity foundUser = new UserEntity();
                try {
                    foundUser.setId(rs.getObject("id", UUID.class));
                    foundUser.setUsername(rs.getString("username" ));
                    foundUser.setCurrency(CurrencyValues.valueOf(rs.getString("u.currency" )));
                    foundUser.setFirstname(rs.getString("firstname" ));
                    foundUser.setSurname(rs.getString("surname" ));
                    foundUser.setFullname(rs.getString("full_name" ));
                    foundUser.setPhoto(rs.getBytes("photo" ));
                    foundUser.setPhoto(rs.getBytes("photo_small" ));
                    return foundUser;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            List<FriendshipEntity> requests = user.getFriendshipRequests();
            List<FriendshipEntity> addressee = user.getFriendshipAddressees();
            FriendshipEntity friendship = new FriendshipEntity();
            UserEntity requester = new UserEntity();
            UserEntity userAddressee = new UserEntity();
            UUID requesterId = rs.getObject("requester_id", UUID.class);
            UUID addresseeId = rs.getObject("addressee_id", UUID.class);
            FriendshipStatus status = FriendshipStatus.valueOf(rs.getString("status" ));

            if (requesterId != null) {
                requester.setId(requesterId);
                friendship.setRequester(requester);
            }

            if (addresseeId != null){
                userAddressee.setId(addresseeId);
                friendship.setAddressee(userAddressee);
            }

            if (status != null){
                friendship.setStatus(status);
            }

            if(requesterId.equals(userId)){
                requests.add(friendship);
            }

            if(addresseeId.equals(userId)){
                addressee.add(friendship);
            }

            user.setFriendshipRequests(requests);
            user.setFriendshipAddressees(addressee);
        }
        return user;
    }
}
