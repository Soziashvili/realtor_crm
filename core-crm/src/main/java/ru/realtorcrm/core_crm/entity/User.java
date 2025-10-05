package ru.realtorcrm.core_crm.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.realtorcrm.core_crm.dto.UserDto;

@Data
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String nickname;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    public static User createUserFromDto(UserDto buildingDto) {
        return builder()
                .nickname(buildingDto.getNickname())
                .name(buildingDto.getName())
                .phoneNumber(buildingDto.getPhoneNumber())
                .email(buildingDto.getEmail())
                .build();
    }
}
