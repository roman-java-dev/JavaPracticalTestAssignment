package com.example.javapracticaltestassignment.service.impl;

import com.example.javapracticaltestassignment.entity.User;
import com.example.javapracticaltestassignment.exception.DataProcessingException;
import com.example.javapracticaltestassignment.repository.UserRepository;
import com.example.javapracticaltestassignment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUserSeveralFields(Long userId, Map<String, Object> fields) {
        User userFromDB = getUserIfExists(userId);

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(User.class, key);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, userFromDB, value);
        });
        return userRepository.save(userFromDB);
    }

    @Override
    public User updateUser(Long userId, User user) {
        User userFromDB = getUserIfExists(userId);
        userFromDB.setEmail(user.getEmail());
        userFromDB.setFirstName(user.getFirstName());
        userFromDB.setLastName(user.getLastName());
        userFromDB.setBirthDate(user.getBirthDate());
        userFromDB.setAddress(user.getAddress());
        userFromDB.setPhoneNumber(user.getPhoneNumber());
        return userRepository.save(userFromDB);
    }

    @Override
    public void deleteUser(Long userId) {
        User userFromDB = getUserIfExists(userId);
        userRepository.delete(userFromDB);
    }

    @Override
    public List<User> findUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("\"From\" date should be less than \"To\" date");
        }
        return userRepository.findUsersByBirthDateBetween(fromDate, toDate);
    }

    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new DataProcessingException("Couldn't find user by id: " + userId)
        );
    }
}
