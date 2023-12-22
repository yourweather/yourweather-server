package com.umc.yourweather.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangePasswordRequestDto {

    @NotBlank
    String password;

    @NotBlank
    String newPassword;

    @Builder

    public ChangePasswordRequestDto(String password, String newPassword) {
        this.password = password;
        this.newPassword = newPassword;
    }
}
