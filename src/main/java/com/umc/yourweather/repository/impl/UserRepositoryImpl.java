package com.umc.yourweather.repository.impl;

import com.umc.yourweather.domain.User;
import com.umc.yourweather.repository.UserRepository;
import com.umc.yourweather.repository.jpa.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByRefreshToken(String refreshToken) {
        return userJpaRepository.findByRefreshToken(refreshToken);
    }
}
