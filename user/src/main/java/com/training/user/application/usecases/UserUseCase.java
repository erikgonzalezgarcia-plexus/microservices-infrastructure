package com.training.user.application.usecases;

import com.training.user.application.dto.PurchaseDTO;
import com.training.user.application.dto.UserDTO;
import com.training.user.application.dto.order.OrderDTO;

import java.util.List;

public interface UserUseCase {

    OrderDTO getOrders(Long userID);

    void purchaseItem(PurchaseDTO purchaseDTO);

    UserDTO addUser(UserDTO userDTO);

    List<UserDTO> getAll();

    UserDTO getByEmail(String email);
}
