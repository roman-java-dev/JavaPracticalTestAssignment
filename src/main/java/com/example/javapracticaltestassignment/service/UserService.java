package com.example.javapracticaltestassignment.service;

import com.example.javapracticaltestassignment.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UserService {
    User addUser(User user);

    User updateUserSeveralFields(Long userId, Map<String, Object> fields);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);

    List<User> findUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate);
}
