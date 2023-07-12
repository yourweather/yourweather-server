package com.umc.yourweather.controller;

import com.umc.yourweather.dto.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    @GetMapping("/send")
    public ResponseDto<Void> send() {

        return ResponseDto.success("이메일 전송 완료.");
    }
}
