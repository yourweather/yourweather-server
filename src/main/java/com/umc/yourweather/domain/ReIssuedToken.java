package com.umc.yourweather.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReIssuedToken {
    private final String accessToken;
    private final String refreshToken;
}
