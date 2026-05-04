package com.training.user;

import com.training.user.application.controller.UserController;
import com.training.user.application.dto.UserDTO;
import com.training.user.application.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturnUserWhenEmailExists() throws Exception {
        var user = new UserDTO("John", "Doe", 30, "john.doe@example.com", "Main Street 1");

        BDDMockito.given(userService.getByEmail("john.doe@example.com"))
                .willReturn(user);

        mockMvc.perform(get("/api/users/search")
                        .param("email", "john.doe@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void shouldReturnNotFoundWhenEmailDoesNotExist() throws Exception {
        BDDMockito.given(userService.getByEmail("missing@example.com"))
                .willThrow(new NoSuchElementException("User not found"));

        mockMvc.perform(get("/api/users/search")
                        .param("email", "missing@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsBlank() throws Exception {
        mockMvc.perform(get("/api/users/search")
                        .param("email", " ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
