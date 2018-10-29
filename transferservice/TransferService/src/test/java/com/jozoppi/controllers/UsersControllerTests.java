/**
 * @author Giorgio Zoppi
 * @email giorgio.zoppi@gmail.com
 *  Here we provide integrations tests about the UserController,
 *  we exclude the sign-up test since it will be covered in
 *  the python register tool, inside the folder python.
 *  As test tool we use MockMvc.
 * @see register.py
 */
package com.jozoppi.controllers;

import com.jozoppi.models.User;
import com.jozoppi.repositories.UsersRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.jozoppi.util.JsonUtil.asJsonString;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UsersControllerTests
{

    private MockMvc mockMvc;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersController usersController;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
    }
    @Test
    public void Should_Fail_WhenWeakPassword() throws Exception
    {
        User sourceUser = new User();
        sourceUser.setUsername("Liuk");
        sourceUser.setPassword("372");
        when(usersRepository.save(any(User.class))).thenReturn(sourceUser);
        when(usersRepository.findByUsername(sourceUser.getUsername())).thenReturn(Optional.of(sourceUser));
        mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sourceUser)))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void Should_Return_ValidUser() throws Exception
    {
        User sourceUser = new User();
        sourceUser.setUsername("giorgiozoppi");
        sourceUser.setPassword("328982982ADv");
        when(usersRepository.findByUsername(sourceUser.getUsername())).thenReturn(Optional.of(sourceUser));
        mockMvc.perform(get("/users/"+sourceUser.getUsername())).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }
    @Test
    public void Should_Fail_WhenUserNotValid() throws Exception
    {
        User sourceUser = new User();
        sourceUser.setUsername("giorgiozoppi");
        sourceUser.setPassword("328982982ADv");
        when(usersRepository.findByUsername(sourceUser.getUsername())).thenReturn(Optional.of(sourceUser));
        mockMvc.perform(get("/users/lucy")).
                andExpect(status().isBadRequest());
    }
}
