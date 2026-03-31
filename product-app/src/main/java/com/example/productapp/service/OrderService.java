package com.example.productapp.service;

import com.example.productapp.entity.Order;
import com.example.productapp.model.CartItem;
import java.util.Collection;

public interface OrderService {
    Order placeOrder(Collection<CartItem> cartItems, String username);
}
