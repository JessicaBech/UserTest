package com.example.user;

import com.example.user.user.UserEntities;
import com.example.user.user.UserRepository;
import com.example.user.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.stream.Stream;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserApplicationTests {

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	@Test
	public void getUsersTest(){
		when(userRepository.findAll()).thenReturn(Stream
				.of(new UserEntities(1L,"Jessica","Bechara","Ayto"),new UserEntities(2L,"Simon","Bechara","Ayto")).collect(Collectors.toList()));
		assertEquals(2, userService.getAllUsers().size());
	}

	@Test
	public void insertUserTest(){
		UserEntities user1= new UserEntities(1L, "Jessica", "Bech", "Ayto");
		when(userRepository.save(user1)).thenReturn(user1);
		assertEquals(user1, userService.insertUser(user1));
	}

	@Test
	public void deleteUserTest(){
		UserEntities user1= new UserEntities(1L, "Jessica", "Bech", "Ayto");
		userService.deleteUser(user1.getId());
		verify(userRepository, times(1)).deleteById(user1.getId());
	}

}
