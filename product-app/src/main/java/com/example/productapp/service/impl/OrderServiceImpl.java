package com.example.productapp.service.impl;

import com.example.productapp.entity.Order;
import com.example.productapp.entity.OrderDetail;
import com.example.productapp.entity.Product;
import com.example.productapp.entity.User;
import com.example.productapp.model.CartItem;
import com.example.productapp.repository.OrderDetailRepository;
import com.example.productapp.repository.OrderRepository;
import com.example.productapp.repository.ProductRepository;
import com.example.productapp.repository.UserRepository;
import com.example.productapp.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Order placeOrder(Collection<CartItem> cartItems, String username) {
        User user = userRepository.findByUsername(username);
        
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        
        double total = 0;
        List<OrderDetail> details = new ArrayList<>();
        
        for (CartItem item : cartItems) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                OrderDetail detail = new OrderDetail(order, product, item.getQuantity(), item.getPrice());
                details.add(detail);
                total += item.getPrice() * item.getQuantity();
            }
        }
        
        order.setTotalAmount(total);
        order.setDetails(details);
        
        return orderRepository.save(order);
    }
}
