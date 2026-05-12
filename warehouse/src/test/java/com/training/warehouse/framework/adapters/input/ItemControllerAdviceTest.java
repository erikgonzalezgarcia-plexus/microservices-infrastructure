package com.training.warehouse.framework.adapters.input;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.training.warehouse.application.ports.input.ItemInputPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ItemController.class)
@Import(GlobalRestControllerAdvice.class)
class ItemControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemInputPort itemInputPort;

    @Test
    @DisplayName("Debe devolver 400 cuando price tiene un formato inválido")
    void shouldReturnBadRequestWhenPriceHasInvalidFormat() throws Exception {
        mockMvc.perform(
                        post("/api/item")
                                .param("title", "Item 1")
                                .param("description", "Desc")
                                .param("price", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Parameter 'price' has invalid value 'abc'"))
                .andExpect(jsonPath("$.path").value("/api/item"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Debe devolver 400 cuando falta un parámetro obligatorio")
    void shouldReturnBadRequestWhenRequiredParameterIsMissing() throws Exception {
        mockMvc.perform(
                        post("/api/item")
                                .param("title", "Item 1")
                                .param("description", "Desc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Required parameter 'price' is missing"))
                .andExpect(jsonPath("$.path").value("/api/item"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Debe devolver 500 con mensaje genérico cuando ocurre un error inesperado")
    void shouldReturnInternalServerErrorWhenUnexpectedExceptionOccurs() throws Exception {
        given(itemInputPort.getItemById(anyLong())).willThrow(new RuntimeException("database down"));

        mockMvc.perform(get("/api/item/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.path").value("/api/item/1"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
