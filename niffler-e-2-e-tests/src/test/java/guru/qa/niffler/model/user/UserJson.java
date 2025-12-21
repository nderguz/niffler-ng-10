package guru.qa.niffler.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.TestData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("fullname")
    private String fullname;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("photoSmall")
    private String photoSmall;
    @JsonProperty("friendshipStatus")
    private FriendshipStatus friendshipStatus;
    @JsonIgnore
    private TestData testData;

    public static UserJson fromEntity(UserEntity entity, FriendshipStatus friendshipStatus) {
        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstname(),
                entity.getSurname(),
                entity.getFullname(),
                entity.getCurrency(),
                entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null,
                entity.getPhotoSmall() != null && entity.getPhotoSmall().length > 0 ? new String(entity.getPhotoSmall(), StandardCharsets.UTF_8) : null,
                friendshipStatus,
                null
        );
    }

    public UserJson(String username, TestData testData) {
        this.username = username;
        this.testData = testData;
    }

    public UserJson addTestData(TestData testData) {
        this.testData = testData;
        return this;
        // Due to User extension refactor this code was removed
//        return new UserJson(
//                id,
//                username,
//                firstname,
//                surname,
//                fullname,
//                currency,
//                photo,
//                photoSmall,
//                friendshipStatus,
//                testData
//        );
    }
}
