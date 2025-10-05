package ru.realtorcrm.core_crm.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    @NotEmpty(message = "Ник не должен быть пустым")
    private String nickname;

    @NotEmpty(message = "Имя пользователя обязательно")
    private String name;

    @NotEmpty(message = "Номер телефона обязателен")
    private String phoneNumber;

    @NotEmpty(message = "Email обязателен")
    private String email;
}
