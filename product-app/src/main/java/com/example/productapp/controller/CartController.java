package com.example.productapp.controller;

import com.example.productapp.entity.Product;
import com.example.productapp.model.CartItem;
import com.example.productapp.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final ProductService productService;
    private final com.example.productapp.service.OrderService orderService;

    public CartController(ProductService productService, com.example.productapp.service.OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, java.security.Principal principal, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        Map<Long, CartItem> cart = getCartFromSession(session);
        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống!");
            return "redirect:/cart";
        }

        orderService.placeOrder(cart.values(), principal.getName());
        session.removeAttribute("cart"); // Xóa giỏ hàng sau khi đặt thành công
        redirectAttributes.addFlashAttribute("message", "Đặt hàng thành công!");
        return "redirect:/products";
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Map<Long, CartItem> cart = getCartFromSession(session);
        double total = cart.values().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("cart", cart.values());
        model.addAttribute("total", total);
        return "cart/view";
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        Product product = productService.getById(id);
        if (product != null) {
            Map<Long, CartItem> cart = getCartFromSession(session);
            CartItem item = cart.getOrDefault(id, new CartItem(id, product.getName(), product.getPrice(), product.getImage(), 0));
            item.setQuantity(item.getQuantity() + 1);
            cart.put(id, item);
            session.setAttribute("cart", cart);
            redirectAttributes.addFlashAttribute("message", "Đã thêm " + product.getName() + " vào giỏ hàng thành công!");
        }
        return "redirect:/products";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        Map<Long, CartItem> cart = getCartFromSession(session);
        cart.remove(id);
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam("id") Long id, @RequestParam("quantity") int quantity, HttpSession session) {
        Map<Long, CartItem> cart = getCartFromSession(session);
        if (cart.containsKey(id) && quantity > 0) {
            cart.get(id).setQuantity(quantity);
        } else if (quantity <= 0) {
            cart.remove(id);
        }
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @SuppressWarnings("unchecked")
    private Map<Long, CartItem> getCartFromSession(HttpSession session) {
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        return cart;
    }
}
