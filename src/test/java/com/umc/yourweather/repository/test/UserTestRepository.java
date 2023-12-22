package com.umc.yourweather.repository.test;

import com.umc.yourweather.domain.entity.User;
import com.umc.yourweather.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserTestRepository implements UserRepository {

    List<User> repository = new ArrayList<>();

    @Override
    public Optional<User> findByEmail(String email) {
        for (User user : repository) {
            if (user.getEmail().equals(email)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByRefreshToken(String refreshToken) {
        for (User user : repository) {
            if (user.getEmail().equals(refreshToken)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        repository.add(user);
        return user;
    }

    @Override
    public void deleteExpiredUser() {
        for (User user : repository) {
            if (!user.isActivate()) {
                repository.remove(user);
            }
        }
        return;
    }
}
