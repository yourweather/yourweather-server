package com.umc.yourweather.service;

import com.umc.yourweather.auth.CustomUserDetails;
import com.umc.yourweather.domain.Role;
import com.umc.yourweather.domain.User;
import com.umc.yourweather.dto.ChangePasswordDto;
import com.umc.yourweather.dto.UserResponseDto;
import com.umc.yourweather.dto.SignupRequestDto;
import com.umc.yourweather.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signup(@Valid SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();

        if (password == null) {
            password = UUID.randomUUID().toString();
        }

        password = passwordEncoder.encode(password);
        String nickname = signupRequestDto.getNickname();
        String platform = signupRequestDto.getPlatform();

        // 이메일 중복 검증 로직
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 해당 이메일로 가입된 유저가 존재합니다.");
        }

        User user = User.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .platform(platform)
            .role(Role.ROLE_USER)
            .isActivate(true)
            .build();
        userRepository.save(user);
        return "회원 가입 완료";
    }

    public UserResponseDto mypage(CustomUserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUser().getEmail())
            .orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));
        return new UserResponseDto(user.getNickname(), user.getEmail());
    }

    public String changePassword(ChangePasswordDto changePasswordDto,
        CustomUserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUser().getEmail())
            .orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        user.changePassword(changePasswordDto.getPassword());
        return "비밀번호 변경 완료";
    }

    public String withdraw(CustomUserDetails userDetails) {
  

      User user = userRepository.findByEmail(userDetails.getUser().getEmail()).orElseThrow(
            () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        return new UserResponseDto(user.getNickname(), user.getEmail());
    }
}
