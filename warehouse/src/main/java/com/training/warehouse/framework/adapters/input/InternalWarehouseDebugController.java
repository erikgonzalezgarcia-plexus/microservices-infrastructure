package com.training.warehouse.framework.adapters.input;

import com.training.warehouse.application.ports.input.ItemInputPort;
import com.training.warehouse.domain.entity.Item;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/warehouse/debug")
@ConditionalOnProperty(prefix = "warehouse.debug.endpoints", name = "enabled", havingValue = "true")
public class InternalWarehouseDebugController {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 50;
    private static final int MAX_SIZE = 200;

    private final ItemInputPort itemInputPort;

    public InternalWarehouseDebugController(ItemInputPort itemInputPort) {
        this.itemInputPort = itemInputPort;
    }

    @GetMapping("/items")
    public Page<Item> getItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageRequest.of(
                sanitizePage(page),
                sanitizeSize(size),
                buildSort(sortBy, direction));

        return itemInputPort.getAll(pageable);
    }

    private int sanitizePage(int page) {
        return Math.max(page, DEFAULT_PAGE);
    }

    private int sanitizeSize(int size) {
        if (size <= 0) {
            return DEFAULT_SIZE;
        }
        return Math.min(size, MAX_SIZE);
    }

    private Sort buildSort(String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return Sort.by(sortDirection, normalizeSortBy(sortBy));
    }

    private String normalizeSortBy(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return "id";
        }
        return sortBy;
    }
}
