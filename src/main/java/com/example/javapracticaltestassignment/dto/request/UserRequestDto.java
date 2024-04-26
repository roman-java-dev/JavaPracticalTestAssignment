package com.example.javapracticaltestassignment.dto.request;

import com.example.javapracticaltestassignment.lib.MinAge;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

import static com.example.javapracticaltestassignment.util.ConstantsUtil.EMAIL_PATTERN;
import static com.example.javapracticaltestassignment.util.ConstantsUtil.PHONE_NUMBER_PATTERN;

@Data
@Builder
public class UserRequestDto {
    @NotBlank(message = "{validation.email.required}")
    @Pattern(regexp = EMAIL_PATTERN, message = "{validation.email.invalid}")
    private String email;
    @NotBlank(message = "{validation.firstName.required}")
    private String firstName;
    @NotBlank(message = "{validation.lastName.required}")
    private String lastName;
    @NotNull(message = "{validation.birthDate.required}")
    @Past(message = "{validation.birthDate.past}")
    @MinAge
    private LocalDate birthDate;
    @Size(max = 255, message = "{validation.address.maxSize}")
    private String address;
    @Pattern(regexp = PHONE_NUMBER_PATTERN, message = "{validation.phoneNumber.invalid}")
    private String phoneNumber;
}
