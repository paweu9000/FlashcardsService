package com.flashcard.flashback.user.data;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class UserDto {
    @NotBlank
    @NotNull
    @NotEmpty
    @Min(4)
    @Max(30)
    private String login;
    @NotBlank
    @NotNull
    @NotEmpty
    @Min(2)
    @Max(30)
    private String username;
    @NotNull
    @NotBlank
    @NotEmpty
    @Email
    private String email;
    @NotBlank
    @NotNull
    @NotEmpty
    @Min(6)
    private String password;
}
