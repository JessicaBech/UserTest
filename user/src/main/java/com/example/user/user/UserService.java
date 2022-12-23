package com.example.user.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    public UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntities> getAllUsers(){
        List<UserEntities> users = userRepository.findAll();
        System.out.println("Users: " + users);
        return users;
    }

    public UserEntities getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    public UserEntities insertUser(UserEntities user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserEntities updateUser(Long userId, UserEntities userDetails) {
        UserEntities user = userRepository.findById(userId).get();
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setAddress(userDetails.getAddress());

        return userRepository.save(user);
    }
}
