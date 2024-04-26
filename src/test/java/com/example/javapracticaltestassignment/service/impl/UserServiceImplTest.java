package com.example.javapracticaltestassignment.service.impl;

import com.example.javapracticaltestassignment.entity.User;
import com.example.javapracticaltestassignment.exception.DataProcessingException;
import com.example.javapracticaltestassignment.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("givenAddUser_whenValidInput_thenSuccessful")
    public void testAddUser_ok() {
        User user = User.builder()
                .email("Bob@gmail.com")
                .firstName("Bob")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990,1,1))
                .address("123 Main Street").build();

        when(userRepository.save(any())).thenReturn(user);

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("givenUpdateUserSeveralFields_whenValidInputAndUserExistsInDb_thenSuccessful")
    public void testUpdateUserSeveralFields_ok() {
        Long userId = 1L;
        Map<String, Object> fields = new HashMap<>();
        fields.put("firstName", "John");
        fields.put("lastName", "Smith");

        User userFromDB = User.builder()
                .email("Bob@gmail.com")
                .firstName("Bob")
                .lastName("Doe").build();
        User expectedUser = User.builder()
                .email("Bob@gmail.com")
                .firstName("John")
                .lastName("Smith").build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userFromDB));
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        User actualUser = userService.updateUserSeveralFields(userId, fields);

        assertNotNull(actualUser);
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        verify(userRepository).save(expectedUser);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("givenUpdateUser_whenValidInputAndUserExistsInDb_thenSuccessful")
    public void testUpdateUser_ok() {
        Long userId = 1L;
        User userFromDB = User.builder()
                .email("Bob@gmail.com")
                .firstName("Bob")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990,1,1))
                .address("123 Main Street").build();
        User expectedUser = User.builder()
                .email("Lui@gmail.com")
                .firstName("Lui")
                .lastName("Golf")
                .birthDate(LocalDate.of(2000,2,3))
                .address("12/1 West Street").build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userFromDB));
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        User actualUser = userService.updateUser(userId, expectedUser);

        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
        verify(userRepository).save(expectedUser);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("givenDeleteUser_whenUserExistsInDb_thenSuccessful")
    public void testDeleteUser_ok() {
        Long userId = 1L;
        User userFromDB = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userFromDB));
        doNothing().when(userRepository).delete(userFromDB);

        userService.deleteUser(userId);

        verify(userRepository).delete(userFromDB);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("givenFindUsersByBirthDateRange_whenValidInput_thenSuccessful")
    public void testFindUsersByBirthDateRange_ok() {
        LocalDate fromDate = LocalDate.of(1990, 2, 3);
        LocalDate toDate = LocalDate.of(2000, 2, 3);
        when(userRepository.findUsersByBirthDateBetween(fromDate, toDate)).thenReturn(List.of(new User()));

        List<User> userList = userService.findUsersByBirthDateRange(fromDate, toDate);

        assertNotNull(userList);
        verify(userRepository).findUsersByBirthDateBetween(fromDate, toDate);
    }

    @Test
    @DisplayName("givenFindUsersByBirthDateRange_whenInvalidInput_thenGetException")
    public void testFindUsersByBirthDateRange_invalidInput_notOk() {
        LocalDate fromDate = LocalDate.of(2005, 2, 3);
        LocalDate toDate = LocalDate.of(2000, 2, 3);

        assertThrows(IllegalArgumentException.class,
                () -> userService.findUsersByBirthDateRange(fromDate, toDate));
    }

    @Test
    @DisplayName("givenDeleteUser_whenUserNotExistsInDb_thenGetException")
    public void testDeleteUser_userNotExistsInDB_notOk() {
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(DataProcessingException.class,
                () -> userService.deleteUser(userId));
    }
}
