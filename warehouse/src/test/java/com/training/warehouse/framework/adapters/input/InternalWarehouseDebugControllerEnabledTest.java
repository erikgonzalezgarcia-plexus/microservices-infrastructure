package com.training.warehouse.framework.adapters.input;

import com.training.warehouse.application.ports.input.ItemInputPort;
import com.training.warehouse.domain.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "warehouse.debug.endpoints.enabled=true")
@AutoConfigureMockMvc
class InternalWarehouseDebugControllerEnabledTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemInputPort itemInputPort;

    @Test
    void shouldReturnPagedItemsWhenDebugEndpointIsEnabled() throws Exception {
        List<Item> items = List.of(
                new Item("Keyboard", "Mechanical keyboard", new BigDecimal("99.99")),
                new Item("Mouse", "Wireless mouse", new BigDecimal("49.99")));

        when(itemInputPort.getAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(items, PageRequest.of(0, 2), items.size()));

        mockMvc.perform(get("/internal/warehouse/debug/items")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sortBy", "id")
                        .param("direction", "asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Keyboard"))
                .andExpect(jsonPath("$.content[1].title").value("Mouse"));

        verify(itemInputPort).getAll(any(Pageable.class));
    }

    @Test
    void shouldClampInvalidPaginationValues() throws Exception {
        when(itemInputPort.getAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 50), 0));

        mockMvc.perform(get("/internal/warehouse/debug/items")
                        .param("page", "-10")
                        .param("size", "500")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemInputPort).getAll(any(Pageable.class));
    }
}
