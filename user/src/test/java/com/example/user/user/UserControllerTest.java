package com.example.user.user;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@RunWith(SpringRunner.class)
class UserControllerTest {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private Long ID = 1L;

    @Test
    @DisplayName("It should return All users")
    public void getUsers() throws Exception{
        UserEntities user1= new UserEntities();
        user1.setId(1L);
        user1.setFirstName("Jessica");
        user1.setLastName("Bech");
        user1.setAddress("Ayto");

        UserEntities user2= new UserEntities();
        user2.setId(2L);
        user2.setFirstName("Simon");
        user2.setLastName("Bech");
        user2.setAddress("Ayto");

        List<UserEntities> allUsers = Arrays.asList(user1,user2);

        given(userService.getAllUsersFromDb()).willReturn(allUsers);

        mvc.perform(get("/api/all/database")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("It should return a user")
    public void getUserById() throws Exception{
        UserEntities user= new UserEntities();
        user.setId(1L);
        user.setFirstName("Jessica");
        user.setLastName("Bech");
        user.setAddress("Ayto");

        when(userService.getUserById(eq(1L))).thenReturn(user);

        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(
                "/api/user/{userId}", 1L).accept(
                MediaType.APPLICATION_JSON)).andReturn();

        String expected = "{\"id\":"+ ID + ",\"firstName\":\"Jessica\",\"lastName\":\"Bech\",\"address\":\"Ayto\"}";
        System.out.println("User with ID 1: " + expected);
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("It should insert a user")
    public void postUser() throws Exception{
        UserEntities user= new UserEntities(1L,"Jessica", "Bech", "Ayto");

        when(userService.insertUser(any())).thenReturn(user);

        mvc.perform(post("/api/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"firstName\":\"Jessica\",\"lastName\":\"Bech\",\"address\":\"Ayto\"}"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("It should get all users in cache")
    public void getAllUsersInCache() throws Exception {
        MvcResult result = mvc.perform(
                        get("/api/all/cache")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Assertions.assertNotNull(content);

        JsonElement output = JsonParser.parseString(content);
        System.out.println("Users: " + gson.toJson(output));

    }
    @Test
    @DisplayName("It should get refresh cache")
    public void refreshUsers() throws Exception {
        MvcResult result = mvc.perform(
                        get("/api/refreshCache")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Assertions.assertNotNull(content);

        System.out.println("OUTPUT: " + content);
    }
}
