package com.example.javapracticaltestassignment.repository;

import com.example.javapracticaltestassignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByBirthDateBetween(LocalDate fromDate, LocalDate toDate);
}
