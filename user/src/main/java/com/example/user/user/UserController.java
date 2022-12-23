package com.example.user.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/all")
    public List<UserEntities> getUsers(){
        return userService.getAllUsers();
    }

    @GetMapping(value = "/user/{userId}")
    public UserEntities getUserById(@PathVariable(value = "userId") Long id) {
        return userService.getUserById(id);
    }

    @PostMapping (value = "/add", consumes = "application/json", produces = "application/json")
//    @ResponseStatus(HttpStatus.CREATED)
    public UserEntities postUser(@RequestBody UserEntities user) {
        return userService.insertUser(user);
    }

    @DeleteMapping(value = "/user/{userId}")
    public void deleteUser(@PathVariable(value = "userId") Long id) {
        userService.deleteUser(id);
    }


    @PutMapping(value="/user/{userId}")
    public UserEntities modifyUser(@PathVariable(value = "userId") Long id, @RequestBody UserEntities userDetails) {
        return userService.updateUser(id, userDetails);
    }
}
