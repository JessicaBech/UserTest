package com.example.user.user;

import java.util.Collection;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "User Controller")
public class UserController {

    @Autowired
    private UserService userService;



/*
    -------------------------DATABASE-----------------------------
*/
//    @Cacheable(value="users")
    @GetMapping(value = "/all/database")
    public List<UserEntities> getUsers(){
        return userService.getAllUsersFromDb();
    }

//    @Cacheable(value="users", key="#id")
    @GetMapping(value = "/user/{userId}")
    public UserEntities getUserById(@PathVariable(value = "userId") Long id) {
        return userService.getUserById(id);
    }

    @PostMapping (value = "/add", consumes = "application/json", produces = "application/json")
    public UserEntities postUser(@RequestBody UserEntities user) {
        return userService.insertUser(user);
    }

//    @CacheEvict(value = "users", key="#id")
    @DeleteMapping(value = "/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "userId") Long id) {
        UserEntities user= userService.getUserById(id);
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body( "User "+ id + " was Deleted");
    }

//    @CachePut(value = "users", key = "#id")
    @PutMapping(value="/user/{userId}")
    public UserEntities modifyUser(@PathVariable(value = "userId") Long id, @RequestBody UserEntities userDetails) {
        return userService.updateUser(id, userDetails);
    }


/*
    -------------------------CACHING-----------------------------
*/

    @GetMapping(value = "/all/cache")
    public List<UserEntities> getAllUsersInCache(){
        return userService.getAllUsersFromCache();
    }
    @GetMapping(value = "/refreshCache")
    public ResponseEntity<?> refreshUsers() {
    /*
    Refresh keywords cache
    */
        userService.refreshUserCache();
    /*
    Return success response
    */
        return ResponseEntity.status(HttpStatus.OK).body("Refreshed");

    }
}


//    @PostMapping (value = "/add/cache")
//    public UserEntities postUsersInCache(@RequestBody UserEntities user) {
//        return userService.insertUserInCache(user);
//    }