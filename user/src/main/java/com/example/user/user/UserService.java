package com.example.user.user;

import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;
import org.redisson.api.RMapCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private RedissonClient redissonClient;
    private UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


/*
    -----------------------CACHING------------------------
*/

    public List<UserEntities> getAllUsersFromCache(){
        RMapCache<Long, UserEntities> usersMap = redissonClient.getMapCache("USERS");
        Collection<UserEntities> usersList = usersMap.values();
        List<UserEntities> filteredUsersList = usersList
                .parallelStream()
                .collect(Collectors.toList());
        return filteredUsersList;
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    @EventListener(ApplicationReadyEvent.class)
    public void refreshUserCache() {

        RMapCache<Long, UserEntities> userMapCache = redissonClient.getMapCache("USERS");

        long executionTime = System.currentTimeMillis();
        userMapCache.clear();

        getAllUsersFromDb().parallelStream().forEach(this::addUserInCache);
    }


    @Value("${spring.cache.redis.time-to-live}")
    private Long TTL;
    private void addUserInCache(UserEntities user) {

        RMapCache<Long, UserEntities> userMap = redissonClient.getMapCache("USERS");

        userMap.put(
                user.getId(),
                user,
                TTL,
                TimeUnit.MILLISECONDS);
    }

/*
    -------------------------DATABASE-----------------------------
*/
    public List<UserEntities> getAllUsersFromDb(){
        List<UserEntities> users = userRepository.findAll();
        return users;
    }
    public UserEntities getUserById(Long id) {return userRepository.findById(id).get();}

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


//    public UserEntities insertUserInCache(UserEntities user) {
//        RMapCache<String, UserEntities> userMap = redissonClient.getMapCache("USERS");
//
//        userMap.put(
//                String.valueOf(user.getId()),
//                user,
//                TTL,
//                TimeUnit.MILLISECONDS);
//        return user;
//    }