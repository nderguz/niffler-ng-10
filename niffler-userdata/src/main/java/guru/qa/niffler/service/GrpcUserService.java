package guru.qa.niffler.service;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.*;
import guru.qa.niffler.model.IUserJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.UserJsonBulk;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.PageRequest;

@GrpcService
@RequiredArgsConstructor
public class GrpcUserService extends NifflerUserdataServiceGrpc.NifflerUserdataServiceImplBase {

    private final UserService userService;

    @Override
    public void updateUser(UserMessage request, StreamObserver<UserMessage> responseObserver) {
        var updatedUser = userService.update(UserJson.fromMessage(request));
        responseObserver.onNext(fromJson(updatedUser));
        responseObserver.onCompleted();
    }

    @Override
    public void currentUser(CurrentUserRequest request, StreamObserver<UserMessage> responseObserver) {
        var foundUser = userService.getCurrentUser(request.getUsername());
        responseObserver.onNext(fromJson(foundUser));
        responseObserver.onCompleted();
    }

    @Override
    public void sendInvitation(FriendshipActionRequest request, StreamObserver<UserMessage> responseObserver) {
        var user = userService.createFriendshipRequest(request.getRequester(), request.getAddressee());
        responseObserver.onNext(fromJson(user));
        responseObserver.onCompleted();
    }

    @Override
    public void acceptInvitation(FriendshipActionRequest request, StreamObserver<UserMessage> responseObserver) {
        var user = userService.acceptFriendshipRequest(request.getRequester(), request.getAddressee());
        responseObserver.onNext(fromJson(user));
        responseObserver.onCompleted();
    }

    @Override
    public void declineInvitation(FriendshipActionRequest request, StreamObserver<UserMessage> responseObserver) {
        var user = userService.declineFriendshipRequest(request.getRequester(), request.getAddressee());
        responseObserver.onNext(fromJson(user));
        responseObserver.onCompleted();
    }

    @Override
    public void listUsers(ListUsersRequest request, StreamObserver<UserPageResponse> responseObserver) {
        var usersPage = userService.allUsers(
                request.getUsername(),
                PageRequest.of(request.getPageInfo().getPage(), request.getPageInfo().getSize()),
                request.getSearchQuery()
        );

        var usersList = usersPage.stream()
                .map(this::fromJson)
                .toList();

        responseObserver.onNext(UserPageResponse.newBuilder()
                .setTotalElements(Math.toIntExact(usersPage.getTotalElements()))
                .setTotalPages(usersPage.getTotalPages())
                .setFirst(usersPage.isFirst())
                .setLast(usersPage.isLast())
                .setSize(usersPage.getSize())
                .addAllUsers(usersList)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void friends(FriendsRequest request, StreamObserver<UserPageResponse> responseObserver) {
        var usersPage = userService.friends(request.getUsername(),
                PageRequest.of(request.getPageInfo().getPage(), request.getPageInfo().getSize()),
                request.getSearchQuery());
        var usersList = usersPage.stream()
                .map(this::fromJson)
                .toList();
        responseObserver.onNext(UserPageResponse.newBuilder()
                .setTotalElements(usersPage.getSize())
                .setTotalPages((Math.toIntExact(usersPage.getTotalPages())))
                .setFirst(usersPage.isFirst())
                .setLast(usersPage.isLast())
                .setSize(usersPage.getSize())
                .addAllUsers(usersList)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void removeFriend(FriendshipActionRequest request, StreamObserver<Empty> responseObserver) {
        userService.removeFriend(request.getRequester(), request.getAddressee());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    private @Nonnull UserMessage fromJson(@Nonnull IUserJson userJson) {
        UserMessage.Builder user = UserMessage.newBuilder()
                .setId(userJson.id().toString())
                .setUsername(userJson.username());

        if (userJson.firstname() != null && !userJson.firstname().isBlank()) {
            user.setFirstname(userJson.firstname());
        }

        if (userJson.surname() != null && !userJson.surname().isBlank()) {
            user.setSurname(userJson.surname());
        }

        if (userJson.currency() != null) {
            user.setCurrency(CurrencyValues.valueOf(userJson.currency().name()));
        }

        if (userJson.photo() != null) {
            user.setPhoto(userJson.photo());
        }

        if (userJson.photoSmall() != null) {
            user.setPhotoSmall(userJson.photoSmall());
        }

        if (userJson.friendshipStatus() != null) {
            user.setFriendshipStatus(
                    FriendshipStatus.valueOf(userJson.friendshipStatus().name())
            );
        }

        if (userJson instanceof UserJsonBulk bulk) {
            if (bulk.fullname() != null && !bulk.fullname().isBlank()) {
                user.setFullname(bulk.fullname());
            }
        }
        return user.build();
    }
}
