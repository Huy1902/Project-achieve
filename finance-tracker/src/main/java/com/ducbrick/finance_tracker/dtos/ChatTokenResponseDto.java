package com.ducbrick.finance_tracker.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChatTokenResponseDto {

    @NotBlank
    private String chat_token;
    private int expires_in;
}
