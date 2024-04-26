package com.example.javapracticaltestassignment.controller;

import com.example.javapracticaltestassignment.dto.request.UserRequestDto;
import com.example.javapracticaltestassignment.dto.response.UserResponseDto;
import com.example.javapracticaltestassignment.entity.User;
import com.example.javapracticaltestassignment.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ObjectMapper mapper;

    @PostMapping
    public ResponseEntity<UserResponseDto> add(@RequestBody @Valid UserRequestDto dto) {
        User userToSave = mapper.convertValue(dto, User.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.convertValue(userService.addUser(userToSave), UserResponseDto.class));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id,
                                                  @RequestBody Map<String, Object> fields) {
        return ResponseEntity.ok(
                mapper.convertValue(userService.updateUserSeveralFields(id, fields), UserResponseDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateAll(@PathVariable Long id,
                                                  @RequestBody @Valid UserRequestDto dto) {
        User userToUpdate = mapper.convertValue(dto, User.class);
        return ResponseEntity.ok(
                mapper.convertValue(userService.updateUser(id, userToUpdate), UserResponseDto.class));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUsersByBirthDateRange(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<UserResponseDto> usersResponseDto = userService.findUsersByBirthDateRange(fromDate, toDate).stream()
                .map(user -> mapper.convertValue(user, UserResponseDto.class))
                .toList();
        return ResponseEntity.ok(usersResponseDto);
    }
}
