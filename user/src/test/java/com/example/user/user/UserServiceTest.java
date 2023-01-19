package com.example.user.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    UserService userService;
    @BeforeEach
    void initUseCase() {
        userService = new UserService(userRepository);
    }

    @Test
    void getAllUsers() {
        UserEntities user1 = new UserEntities();
        user1.setId(1L);
        user1.setFirstName("Jessica");
        user1.setLastName("Bech");
        user1.setAddress("Ayto");

        UserEntities user2 = new UserEntities();
        user2.setId(2L);
        user2.setFirstName("Simon");
        user2.setLastName("Bech");
        user2.setAddress("Ayto");

        List<UserEntities> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<UserEntities> fetchedUsers = userService.getAllUsersFromDb();
        assertThat(fetchedUsers.size()).isEqualTo(2);
    }

    @Test
    void getUserById() {
        UserEntities user = new UserEntities();
        user.setId(1L);
        user.setFirstName("Jessica");
        user.setLastName("Bech");
        user.setAddress("Ayto");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserEntities fetchedUser = userService.getUserById(user.getId());
        assertThat(fetchedUser).isEqualTo(user);
    }

    @Test
    void insertUser() {
        UserEntities user= new UserEntities();
        user.setId(1L);
        user.setFirstName("Jessica");
        user.setLastName("Bech");
        user.setAddress("Ayto");

        when(userRepository.save(any(UserEntities.class))).thenReturn(user);
        UserEntities savedUser = userRepository.save(user);
        assertThat(savedUser.getFirstName()).isNotNull();
    }
}