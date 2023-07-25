package com.umc.yourweather.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Weathers")
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_id")
    private Long id;

    @Column(name = "yyyy")
    private int year;

    @Column(name = "mm")
    private int month;

    @Column(name = "dd")
    private int day;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "weather")
    List<Memo> memos = new ArrayList<>();

    @Builder
    public Weather(int year, int month, int day, User user) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.user = user;
    }
}
