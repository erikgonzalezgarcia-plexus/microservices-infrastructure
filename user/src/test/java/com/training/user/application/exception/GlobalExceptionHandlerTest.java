package com.training.user.application.exception;

import com.training.user.application.controller.UserController;
import com.training.user.application.dto.UserDTO;
import com.training.user.application.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Should return homogeneous bad request response when request parameter type is invalid")
    void shouldReturnBadRequestResponseWhenRequestParameterTypeIsInvalid() throws Exception {
        mockMvc.perform(get("/api/users/1/orders")
                        .param("transactionId", "invalid-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("The request could not be processed"));
    }

    @Test
    @DisplayName("Should return forbidden response when security exception is thrown")
    void shouldReturnForbiddenResponseWhenSecurityExceptionIsThrown() throws Exception {
        when(userService.getAll()).thenThrow(new SecurityException("technical forbidden detail"));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCESS_DENIED.name()))
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    @DisplayName("Should return internal error response without exposing technical details")
    void shouldReturnInternalErrorResponseWithoutExposingTechnicalDetails() throws Exception {
        when(userService.addUser(any(UserDTO.class))).thenThrow(new RuntimeException("database connection refused on host foo"));

        mockMvc.perform(post("/api/users/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  \"surname\": \"Doe\",
                                  \"lastname\": \"John\",
                                  \"age\": 30,
                                  \"email\": \"john.doe@test.com\",
                                  \"address\": \"Main Street\"
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(ErrorCode.INTERNAL_ERROR.name()))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }

    @Test
    @DisplayName("Should use api exception contract for managed errors")
    void shouldUseApiExceptionContractForManagedErrors() throws Exception {
        when(userService.getAll()).thenThrow(new ApiException(ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND, "Users not found"));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.RESOURCE_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value("Users not found"));
    }
}
