package com.umc.yourweather.service;

import com.umc.yourweather.auth.CustomUserDetails;
import com.umc.yourweather.domain.MemoManager;
import com.umc.yourweather.domain.DateProvider;
import com.umc.yourweather.domain.entity.Memo;
import com.umc.yourweather.domain.entity.User;
import com.umc.yourweather.domain.entity.Weather;
import com.umc.yourweather.exception.WeatherNotFoundException;
import com.umc.yourweather.repository.MemoRepository;
import com.umc.yourweather.response.*;
import com.umc.yourweather.repository.WeatherRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final MemoRepository memoRepository;
    private final DateProvider dateProvider;


    @Transactional
    public MissedInputResponseDto getNotEnteredDates(CustomUserDetails userDetails) {
        List<LocalDate> lastWeekDates = generateLastWeekDates();
        List<Weather> lastWeekWeathers = weatherRepository.findWeatherByDateBetweenAndUser(
            dateProvider.getOneWeekAgo(), dateProvider.getToday(),
            userDetails.getUser());
        List<LocalDate> missedDates = findNotEnteredDates(lastWeekDates,
            lastWeekWeathers);

        return new MissedInputResponseDto(missedDates);
    }

    public List<LocalDate> generateLastWeekDates() {
        LocalDate startDate = dateProvider.getOneWeekAgo();
        LocalDate endDate = dateProvider.getToday();
        return startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
    }

    List<LocalDate> findNotEnteredDates(List<LocalDate> allDates,
        List<Weather> weathers) {
        Set<LocalDate> enteredDates = weathers.stream()
            .map(Weather::getDate)
            .collect(Collectors.toSet());
        return allDates.stream()
            .filter(date -> !enteredDates.contains(date))
            .collect(Collectors.toList());
    }

    public HomeResponseDto home(CustomUserDetails userDetails) {

        User user = userDetails.getUser();
        LocalDate localDate = LocalDate.now();
        MemoManager memoManager = new MemoManager();

        Weather weather = weatherRepository.findByDateAndUser(localDate, user)
            .orElseThrow(() -> new WeatherNotFoundException("오늘 날짜에 해당하는 날씨 객체가 존재하지 않습니다."));

        List<Memo> memoList = weather.getMemos();
        memoManager.isMemoListEmpty(memoList);
        String imageName = memoManager.getImageName(memoList);

        Memo lastMemo = memoList.get(memoList.size() - 1);
        return HomeResponseDto.builder().nickname(user.getNickname()).status(lastMemo.getStatus())
            .temperature(lastMemo.getTemperature()).imageName(imageName).build();

    }

    @Transactional
    public WeatherResponseDto delete(LocalDate localDate, CustomUserDetails userDetails) {
        Weather weather = weatherRepository.findByDateAndUser(localDate,
	userDetails.getUser())
            .orElseThrow(() -> new WeatherNotFoundException("해당 날짜에 대한 Weather 엔티티가 존재하지 않습니다."));

        WeatherResponseDto result = new WeatherResponseDto(weather);
        weatherRepository.delete(weather);

        return result;
    }

    @Transactional
    public String checkMemoAndDelete(Long weatherId) {
        Weather weather = weatherRepository.findById(weatherId).orElseThrow(()
            -> new WeatherNotFoundException("해당 아이디로 조회되는 Weather 엔티티가 존재하지 않습니다."));

        List<Memo> memoList = weather.getMemos();
        if (memoList.size() > 0) {
            return "";
        }

        weatherRepository.delete(weather);

        return " (해당 날짜의 Memo가 전부 삭제되어 해당 날짜의 Weather 또한 같이 삭제되었습니다.)";
    }

    public void update(Long weatherId) {
        Weather weather = weatherRepository.findById(weatherId).orElseThrow(()
            -> new WeatherNotFoundException("해당 아이디로 조회되는 Weather 엔티티가 존재하지 않습니다."));
        List<Memo> memoList = weather.getMemos();

        Memo memoWithHighestTemperature = memoList.get(0); // Initialize with the first memo

        for (Memo tmpMemo : memoList) {
            if (tmpMemo.getTemperature() > memoWithHighestTemperature.getTemperature()) {
	memoWithHighestTemperature = tmpMemo; // Update if a memo with higher temperature is found
            }
        }
    }

    @Transactional(readOnly = true)
    public WeatherMonthlyResponseDto getMonthlyList(int year, int month,
        CustomUserDetails userDetails) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate lastDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<WeatherItemResponseDto> weatherItemResponseDtoList = new ArrayList<>();
        List<Weather> weatherList = weatherRepository.findByMonthAndUser(userDetails.getUser(),
            startDate, lastDate);

        for (Weather weather : weatherList) {

            Memo highestMemo = memoRepository.findFirstByWeatherIdOrderByTemperatureDesc(
	weather.getId());
            WeatherItemResponseDto weatherItemResponseDto = WeatherItemResponseDto.builder()
	.weatherId(weather.getId())
	.date(weather.getDate())
	.highestStatus(highestMemo.getStatus())
	.highestTemperature(highestMemo.getTemperature())
	.build();

            weatherItemResponseDtoList.add(weatherItemResponseDto);
        }

        return new WeatherMonthlyResponseDto(weatherItemResponseDtoList);
    }
}