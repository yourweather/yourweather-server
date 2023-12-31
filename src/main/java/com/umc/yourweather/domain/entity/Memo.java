package com.umc.yourweather.domain.entity;

import com.umc.yourweather.domain.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "weather_status")
    private Status status; //날씨 상태 enums


    @Column(name = "creation_datetime")
    private LocalDateTime createdDateTime;
    private int temperature;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_id")
    private Weather weather;

    @Builder
    public Memo(Status status, LocalDateTime createdDateTime, int temperature, String content, Weather weather) {
        this.status = status;
        this.createdDateTime = createdDateTime;
        this.temperature = temperature;
        this.content = content;
        this.weather = weather;
    }

    public void update(Status status, int temperature, String content) {
        this.status = status;
        this.temperature = temperature;
        this.content = content;
    }
}