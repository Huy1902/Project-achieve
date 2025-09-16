package com.ducbrick.finance_tracker.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserSectionChatDto {
    @NotBlank
    @JsonProperty("user_id")
    private String userID;
}
