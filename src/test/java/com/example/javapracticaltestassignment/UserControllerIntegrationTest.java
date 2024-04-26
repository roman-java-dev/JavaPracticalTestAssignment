package com.example.javapracticaltestassignment;

import com.example.javapracticaltestassignment.dto.request.UserRequestDto;
import com.example.javapracticaltestassignment.entity.User;
import com.example.javapracticaltestassignment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        user = userService.addUser(User.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(1945, 8, 20))
                .email("jane_s@gmail.com").build());
    }

    @Test
    public void testAddUser_ok() throws Exception {
        UserRequestDto requestDto = UserRequestDto.builder()
                .email("john_doe@gmail.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15)).build();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.birthDate", is("1990-05-15")))
                .andExpect(jsonPath("$.email", is("john_doe@gmail.com")));
    }

    @Test
    public void testUpdateUser_ok() throws Exception {
        Long userId = user.getId();

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("firstName", "Janet");

        mockMvc.perform(patch("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fieldsToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Janet")))
                .andExpect(jsonPath("$.lastName", is("Smith")))
                .andExpect(jsonPath("$.birthDate", is("1945-08-20")))
                .andExpect(jsonPath("$.email", is("jane_s@gmail.com")));
    }

    @Test
    public void testUpdateAllUserFields_ok() throws Exception {
        UserRequestDto updatedDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .email("jo@gmail.com").build();
        Long userId = user.getId();
        String jsonRequest = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.birthDate", is("1990-05-15")))
                .andExpect(jsonPath("$.email", is("jo@gmail.com")));
    }

    @Test
    public void testDeleteUser_ok() throws Exception {
        Long userId = user.getId();

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearchUsersByBirthDateRange_ok() throws Exception {
        userService.addUser(User.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1946, 5, 15))
                .email("jo@gmail.com").build());

        LocalDate fromDate = LocalDate.of(1945, 1, 1);
        LocalDate toDate = LocalDate.of(1947, 12, 31);

        mockMvc.perform(get("/users/search")
                        .param("from", fromDate.toString())
                        .param("to", toDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("Jane")))
                .andExpect(jsonPath("$[0].lastName", is("Smith")))
                .andExpect(jsonPath("$[0].birthDate", is("1945-08-20")))
                .andExpect(jsonPath("$[0].email", is("jane_s@gmail.com")))
                .andExpect(jsonPath("$[1].firstName", is("John")))
                .andExpect(jsonPath("$[1].lastName", is("Doe")))
                .andExpect(jsonPath("$[1].birthDate", is("1946-05-15")))
                .andExpect(jsonPath("$[1].email", is("jo@gmail.com")));
    }

    @Test
    public void testAddUser_invalidUserAge_notOk() throws Exception {
        UserRequestDto requestDto = UserRequestDto.builder()
                .email("john_doe@gmail.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(2008, 5, 15)).build();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSearchUsersByBirthDateRange_invalidInput_notOk() throws Exception {
        LocalDate fromDate = LocalDate.of(2001, 1, 1);
        LocalDate toDate = LocalDate.of(1947, 12, 31);

        mockMvc.perform(get("/users/search")
                        .param("from", fromDate.toString())
                        .param("to", toDate.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateAllUserFields_invalidInput_notOk() throws Exception {
        UserRequestDto updatedDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("")
                .birthDate(LocalDate.of(2026, 5, 15))
                .email("jo.mail.com").build();
        Long userId = user.getId();
        String jsonRequest = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteUser_userNotExistsInDb_notOk() throws Exception {
        Long userId = 9992523L;

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNotFound());
    }
}
