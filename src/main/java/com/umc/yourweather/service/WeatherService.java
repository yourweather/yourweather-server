package com.umc.yourweather.service;

import com.umc.yourweather.auth.CustomUserDetails;
import com.umc.yourweather.domain.User;
import com.umc.yourweather.domain.Weather;
import com.umc.yourweather.dto.WeatherRequestDto;
import com.umc.yourweather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public String create(WeatherRequestDto weatherRequestDto, CustomUserDetails userDetails) {
        Weather weather = Weather.builder()
            .user(userDetails.getUser())
            .year(weatherRequestDto.getYear())
            .month(weatherRequestDto.getMonth())
            .day(weatherRequestDto.getDay())
            .build();

        weatherRepository.save(weather);

        return "날씨 생성 완료";
    }
}
