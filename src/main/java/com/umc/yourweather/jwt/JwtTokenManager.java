package com.umc.yourweather.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.yourweather.domain.entity.User;
import com.umc.yourweather.response.AuthorizationResponseDto;
import com.umc.yourweather.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class JwtTokenManager {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    @Value("${jwt.access.header}")
    private String accessTokenHeader;

    @Value("${jwt.refresh.header}")
    private String refreshTokenHeader;


    //jwt 토큰의 사용자 식별 claim으로는 email을 사용함
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL = "email";
    private static final String BEARER = "Bearer ";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String createAccessToken(User user) {
        Date now = new Date();
        return JWT.create()
            .withSubject(ACCESS_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + accessTokenExpiration))
            .withClaim(EMAIL, user.getEmail())
            .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
            .withSubject(REFRESH_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + refreshTokenExpiration))
            .sign(Algorithm.HMAC512(secretKey));
    }

    public void sendAccessTokenAndRefreshToken(HttpServletResponse response,
        String accessToken,
        String refreshToken) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);

        AuthorizationResponseDto authDto = AuthorizationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        ResponseDto<AuthorizationResponseDto> responseDto = ResponseDto.success("토큰 발급 성공", authDto);

        String result = objectMapper.writeValueAsString(responseDto);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(result);

        log.info("Access Token, Refresh Token 전송");
    }

    public Optional<String> extractToken(String token) {
        return Optional.ofNullable(token)
            .filter(tokenValue -> tokenValue.startsWith(BEARER))
            .map(tokenValue -> tokenValue.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        String token = request.getHeader(accessTokenHeader);

        return extractToken(token);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        String token = request.getHeader(refreshTokenHeader);

        return extractToken(token);
    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            return Optional.ofNullable(
                JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(EMAIL)
                    .asString()
            );
        } catch (Exception e) {
            log.error("유효하지 않은 액세스 토큰입니다.");
            return Optional.empty();
        }
    }

    public boolean isAccessTokenValid(String token) {
        try {
            JWT
                .require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(token);

            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 Acess Token입니다." + e.getMessage());
            return false;
        }
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            JWT
                    .require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(token);

            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 Refresh Token입니다." + e.getMessage());
            return false;
        }
    }

    public void updateRefreshToken(User user, String refreshToken) {
        user.updateRefreshToken(refreshToken);
    }

    public void setValue(String secretKey, Long accessTokenExpiration, Long refreshTokenExpiration) {
        this.secretKey = secretKey;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
}


