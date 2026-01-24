package guru.qa.niffler.test.grpc;

import guru.qa.niffler.grpc.*;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.user.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserdataGrpcTest extends BaseGrpcTest {

    @Test
    @DisplayName("Friends request should return pageable response")
    @User(friends = 5)
    public void friendListShouldBePageable(UserJson user) {
        final var friendResponse = userdataServiceBlockingStub.friends(FriendsRequest.newBuilder()
                .setPageInfo(defaultPageInfo())
                .setUsername(user.getUsername())
                .build());
        step("Total elements should be 10 by default", () -> assertEquals(10, friendResponse.getTotalElements()));
        step("Total pages should be 1 by default", () -> assertEquals(1, friendResponse.getTotalPages()));
        step("Should be first page information", () -> assertTrue(friendResponse.getFirst()));
        step("Should be last page information", () -> assertTrue(friendResponse.getLast()));
        step("Size should be 10 by default", () -> assertEquals(10, friendResponse.getSize()));
    }

    @Test
    @DisplayName("Friendship list should be filtered by search query")
    @User(friends = 5)
    public void friendListFilteredBySearchQuery(UserJson user) {
        final var friendAsFilter = user.getTestData().friends().getFirst();
        final var friendResponse = userdataServiceBlockingStub.friends(FriendsRequest.newBuilder()
                .setPageInfo(defaultPageInfo())
                .setUsername(user.getUsername())
                .setSearchQuery(friendAsFilter.getUsername())
                .build());
        step("Check response contains 1 friend", () -> assertEquals(1, friendResponse.getUsersCount()));
        step("Check username was filtered correctly", () -> assertEquals(friendAsFilter.getUsername(), friendResponse.getUsersList().getFirst().getUsername()));
    }

    @Test
    @DisplayName("Friendship should be removed")
    @User(friends = 1)
    public void removeFriendship(UserJson user) {
        userdataServiceBlockingStub.removeFriend(RemoveFriendRequest.newBuilder()
                .setRequester(user.getUsername())
                .setAddressee(user.getTestData().friends().getFirst().getUsername())
                .build());
        final var friendsCount = userdataServiceBlockingStub.friends(FriendsRequest.newBuilder().
                setUsername(user.getUsername())
                .setPageInfo(defaultPageInfo())
                .build()).getUsersCount();
        step("Check friends list is empty", () -> assertEquals(0, friendsCount));
    }

    @Test
    @DisplayName("Income invitation should be accepted")
    @User(incomeInvitations = 1)
    public void acceptIncomeInvitation(UserJson user) {
        userdataServiceBlockingStub.acceptInvitation(AcceptInvitationRequest.newBuilder()
                .setRequester(user.getUsername())
                .setAddressee(user.getTestData().incomeInvitations().getFirst().getUsername())
                .build());
        final var friends = userdataServiceBlockingStub.friends(FriendsRequest.newBuilder()
                .setUsername(user.getUsername())
                .setPageInfo(defaultPageInfo())
                .build()
        );
        step("Check invitation was accepted", () -> assertEquals(1, friends.getUsersCount()));
        step("Check correct status of friendship", () -> assertEquals(FriendshipStatus.FRIEND.name(), friends.getUsersList().getFirst().getFriendshipStatus().name()));
    }

    @Test
    @DisplayName("Income invitation should be rejected")
    @User(incomeInvitations = 1)
    public void rejectIncomeInvitation(UserJson user) {
        userdataServiceBlockingStub.declineInvitation(
                DeclineInvitationRequest.newBuilder()
                        .setRequester(user.getUsername())
                        .setAddressee(user.getTestData().incomeInvitations().getFirst().getUsername())
                        .build());
        final var incomeInvitationList = userdataServiceBlockingStub.friends(FriendsRequest.newBuilder()
                .setUsername(user.getUsername())
                .setPageInfo(defaultPageInfo())
                .build());
        step("Check friend and invitation lists are empty", () -> assertEquals(0, incomeInvitationList.getUsersCount()));
    }

    @Test
    @DisplayName("Outcome invitation should be sent correctly")
    @User(commonUsers = 1)
    public void sendFriendshipInvitation(UserJson user) {
        userdataServiceBlockingStub.sendInvitation(SendInvitationRequest.newBuilder()
                        .setRequester(user.getUsername())
                        .setAddressee(user.getTestData().commonUsers().getFirst().getUsername())
                .build());
        final var friendsPageResponse = userdataServiceBlockingStub.friends(FriendsRequest.newBuilder()
                        .setUsername(user.getTestData().commonUsers().getFirst().getUsername())
                        .setPageInfo(defaultPageInfo())
                .build());
        step("Check friend and invitation lists are not empty", () -> assertEquals(1, friendsPageResponse.getUsersCount()));
        step("Check correct status of income invitation", () -> assertEquals(FriendshipStatus.INVITE_RECEIVED.name(), friendsPageResponse.getUsersList().getFirst().getFriendshipStatus().name()));
    }

    private PageInfo defaultPageInfo() {
        return PageInfo.newBuilder()
                .setPage(0)
                .setSize(10).build();
    }
}
