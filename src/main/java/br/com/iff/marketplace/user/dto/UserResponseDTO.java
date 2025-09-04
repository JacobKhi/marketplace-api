package br.com.iff.marketplace.user.dto;

import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.enums.UserProfiles;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    @JsonProperty("isActive")
    private boolean isActive;
    private UserProfiles profile;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.isActive = user.isActive();
        this.profile = user.getProfile();
    }
}