package com.flashcard.flashback.user.data;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@Builder
public class UserDto {
    @NotBlank
    @NotNull
    @NotEmpty
    @Length(min = 2, max = 30)
    private String login;
    @NotBlank
    @NotNull
    @NotEmpty
    @Length(min = 2, max = 30)
    private String username;
    @NotNull
    @NotBlank
    @NotEmpty
    @Email
    private String email;
    @NotBlank
    @NotNull
    @NotEmpty
    @Length(min = 6)
    private String password;
}
