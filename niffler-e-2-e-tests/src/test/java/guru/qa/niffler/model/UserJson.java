package guru.qa.niffler.model;

import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.UUID;

public record UserJson(
        UUID id,
        String username,
        String firstName,
        String surname,
        String fullName,
        CurrencyValues currency,
        byte[] photo,
        byte[] photoSmall,
        FriendshipStatus friendState
) {
    public static UserJson fromEntity(UserEntity userEntity) {
        return new UserJson(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getFirstname(),
                userEntity.getSurname(),
                userEntity.getFullname(),
                CurrencyValues.valueOf(userEntity.getCurrency().name()),
                userEntity.getPhoto(),
                userEntity.getPhotoSmall(),
                null
        );
    }
}
