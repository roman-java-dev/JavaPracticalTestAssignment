package com.example.javapracticaltestassignment.dto.response;

import java.time.LocalDate;

public record UserResponseDto(
        String email,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String address,
        String phoneNumber
) {
}
