package com.umc.yourweather.repository;

import com.umc.yourweather.domain.entity.Memo;
import com.umc.yourweather.domain.entity.User;
import com.umc.yourweather.domain.entity.Weather;
import com.umc.yourweather.domain.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemoRepository {

    List<Memo> findByUserAndCreatedDateBetween(User user, LocalDateTime startDateTime,
                                               LocalDateTime endDateTime);

    List<Memo> findSpecificMemoList(User user, Status status, LocalDateTime startDateTime,
                                    LocalDateTime endDateTime);

    Memo save(Memo memo);

    Optional<Memo> findById(Long memoId);

    void delete(Memo memo);

//    List<MemoItemResponseDto> findByDateAndUser(User user, LocalDate localDate);

    List<Memo> findByUserAndWeatherId(User user, Weather weather);

    List<Memo> findByWeatherId(Weather weather);

    Memo findFirstByWeatherIdOrderByTemperatureDesc(Long weatherId);
}
