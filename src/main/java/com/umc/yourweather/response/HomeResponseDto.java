package com.umc.yourweather.response;

import com.umc.yourweather.domain.enums.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
public class HomeResponseDto {

    // 홈 화면은 미정, 후에 픽스되는 대로 필드를 추가할 예정
    private String nickname;
    private Status status;
    private int temperature;
    private String imageName;

    @Builder
    public HomeResponseDto(String nickname, Status status, int temperature, String imageName) {
        this.nickname = nickname;
        this.status = status;
        this.temperature = temperature;
        this.imageName = imageName;
    }
}

// List -> Json 관련 주요 사항
// 필드들은 ObjectMapper가 사용 가능한 접근자 메서드(Getter)를 통해 접근하여 JSON으로 변환됩니다.