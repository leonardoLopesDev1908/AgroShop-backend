package com.dailycodework.agroshop.controller;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dailycodework.agroshop.controller.controllers.UserController;
import com.dailycodework.agroshop.controller.dto.register.AddressRegisterDTO;
import com.dailycodework.agroshop.controller.dto.register.UserRegisterDTO;
import com.dailycodework.agroshop.service.User.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
public class UserControllerTest{

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    IUserService userService;

    @MockitoBean
    ObjectMapper objectMapper;

    @Test
    void shouldRegisterNewUser() throws Exception {
        List<AddressRegisterDTO> address = List.of(
            new AddressRegisterDTO(
                "Rua AA", "Bairro BB", "1234",
                "Complement", "City", "State",
                "91949-123"
            )
        );
        
        UserRegisterDTO user = new UserRegisterDTO(
            "User","new User",
            "newuser@gmail.com",
            "(51)1234-4567","24681012",
            address, "Role");
        
        mockMvc.perform(post("/api/v1/usuarios/cadastro")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("Sucesso!"))
            .andExpect(jsonPath("$.data").exists());
    }
}