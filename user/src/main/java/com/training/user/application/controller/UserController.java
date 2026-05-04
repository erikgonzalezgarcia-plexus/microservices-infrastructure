package com.training.user.application.controller;

import com.training.user.application.dto.PurchaseDTO;
import com.training.user.application.dto.UserDTO;
import com.training.user.application.dto.order.OrderDTO;
import com.training.user.application.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/purchase")
    public void purchaseItem(@RequestBody PurchaseDTO purchaseDTO) {

        logger.info("Received request: POST /purchaseItem with params: {}", purchaseDTO);
        userService.purchaseItem(purchaseDTO);
    }

    @GetMapping("/{userId}/orders")
    public OrderDTO getUserOrders(@PathVariable Long userId, @RequestParam UUID transactionId) {
        logger.info("Received request {} - GET /getUserOrders", transactionId);
        var orders = this.userService.getOrders(userId);
        logger.info("{} - GET /getUserOrders returned {}", transactionId, orders);
        return orders;
    }

    @PostMapping("/new")
    public UserDTO addNewUser(@RequestBody UserDTO userDTO) {
        logger.info("Received request: POST /addNewUser with params: ");
        var newUser = this.userService.addUser(userDTO);
        logger.info("Created new user {}", newUser);
        return newUser;
    }

    @GetMapping
    public List<UserDTO> getAll() {
        logger.info("Received request: GET /getAll");
        var users = this.userService.getAll();
        logger.info("GET /getAll returned {}", users);
        return users;
    }

    @GetMapping("/search")
    public UserDTO getByEmail(@RequestParam String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email must not be blank");
        }

        logger.info("Received request: GET /search for email");
        try {
            return userService.getByEmail(email.trim());
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }
    }
}
