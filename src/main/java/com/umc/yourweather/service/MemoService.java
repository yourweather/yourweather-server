package com.umc.yourweather.service;

import com.umc.yourweather.auth.CustomUserDetails;
import com.umc.yourweather.domain.entity.Memo;
import com.umc.yourweather.domain.entity.User;
import com.umc.yourweather.domain.entity.Weather;
import com.umc.yourweather.repository.MemoRepository;
import com.umc.yourweather.request.MemoRequestDto;
import com.umc.yourweather.response.MemoResponseDto;
import com.umc.yourweather.repository.WeatherRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final WeatherRepository weatherRepository;
    private final MemoRepository memoRepository;

    public MemoResponseDto write(MemoRequestDto memoRequestDto, CustomUserDetails userDetails) {
        LocalDateTime dateTime = LocalDateTime.of(
                memoRequestDto.getYear(),
                memoRequestDto.getMonth(),
                memoRequestDto.getDay(),
                memoRequestDto.getHour(),
                memoRequestDto.getMinute(),
                memoRequestDto.getSecond());

        LocalDate date = dateTime.toLocalDate();

        User user = userDetails.getUser();

        // weather 찾아보고 만약 없으면 새로 등록해줌.
        Weather weather = weatherRepository.findByDateAndUser(date, user)
                .orElseGet(() -> {
                    Weather newWeather = Weather.builder()
                            .user(user)
                            .date(date)
                            .build();

                    return weatherRepository.save(newWeather);
                });

        // MemoRequestDto에 넘어온 정보를 토대로 Memo 객체 생성
        Memo memo = Memo.builder()
                .weather(weather)
                .status(memoRequestDto.getStatus())
                .content(memoRequestDto.getContent())
                .temperature(memoRequestDto.getTemperature())
                .createdDateTime(dateTime)
                .build();

        memoRepository.save(memo);
        return MemoResponseDto.builder()
                .status(memo.getStatus())
                .content(memo.getContent())
                .temperature(memo.getTemperature())
                .build();
    }
}
