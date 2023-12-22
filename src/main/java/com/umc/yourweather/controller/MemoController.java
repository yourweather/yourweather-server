package com.umc.yourweather.controller;

import com.umc.yourweather.api.RequestURI;
import com.umc.yourweather.auth.CustomUserDetails;
import com.umc.yourweather.request.MemoRequestDto;
import com.umc.yourweather.request.MemoUpdateRequestDto;
import com.umc.yourweather.response.MemoDailyResponseDto;
import com.umc.yourweather.response.MemoResponseDto;
import com.umc.yourweather.response.MemoUpdateResponseDto;
import com.umc.yourweather.response.ResponseDto;
import com.umc.yourweather.service.MemoService;
import com.umc.yourweather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(RequestURI.MEMO_URI)
public class MemoController {

    private final MemoService memoService;
    private final WeatherService weatherService;

    @PostMapping("/write")
    @Operation(summary = "메모 작성", description = "메모 작성을 위한 API입니다. 전달받은 요청 데이터들을 참조하여 메모를 저장합니다.")
    public ResponseDto<MemoResponseDto> write(
            @RequestBody @Valid MemoRequestDto memoRequestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Optional<MemoResponseDto> memoResponse = memoService.write(memoRequestDto, userDetails);

        return memoResponse.map(memoResponseDto -> ResponseDto.success("메모 저장 완료", memoResponseDto))
                .orElseGet(() -> ResponseDto.fail(HttpStatus.BAD_REQUEST,
                        "메모 저장 실패: 메모는 10개 이상 기록할 수 없습니다."));
    }

    @GetMapping("/daily/{weatherId}")
    @Operation(summary = "하루 치 메모 반환", description = "하루치 메모 리스트를 반환하기 위한 API입니다. weather-controller에 있는 monthly API의 반환 값에 있는 weatherId를 이용해서 조회합니다. ")
    public ResponseDto<MemoDailyResponseDto> daily(@PathVariable Long weatherId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseDto.success(weatherId + "번의 메모 리스트 조회 성공",
                memoService.getDailyList(weatherId, userDetails));
    }

    @Operation(summary = "특정 메모 반환", description = "특정 메모를 반환하기 위한 API입니다. 특정 날씨 List의 반환 값에 있는 memoId를 이용해서 조회합니다. ")
    @GetMapping("/{memoId}")
    public ResponseDto<MemoResponseDto> getMemo(@PathVariable Long memoId) {

        return ResponseDto.success(memoId + "번 메모 조회 성공", memoService.getOneMemo(memoId));
    }


    @Operation(summary = "메모 수정", description = "메모 수정을 위한 API입니다.")
    @PutMapping("/update/{memoId}")
    public ResponseDto<MemoUpdateResponseDto> update(@PathVariable Long memoId,
            @RequestBody MemoUpdateRequestDto requestDto) {
        return ResponseDto.success("메모 수정 완료", memoService.update(memoId, requestDto));
    }

    @Operation(summary = "메모 삭제", description = "메모 삭제을 위한 API입니다.")
    @DeleteMapping("/delete/{memoId}")
    public ResponseDto<Void> delete(@PathVariable Long memoId) {
        Long weatherId = memoService.delete(memoId);
        weatherService.update(weatherId);
        String comment = weatherService.checkMemoAndDelete(weatherId);
        return ResponseDto.success("메모 삭제 완료" + comment);
    }
}