package com.example.productapp;

import com.example.productapp.entity.Category;
import com.example.productapp.entity.User;
import com.example.productapp.repository.CategoryRepository;
import com.example.productapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataLoader(CategoryRepository categoryRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category(null, "Điện thoại", null));
            categoryRepository.save(new Category(null, "Laptop", null));
        }

        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123"));
            admin.setRole("ADMIN"); // Sẽ được load thành ROLE_ADMIN
            userRepository.save(admin);

            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("123"));
            user.setRole("USER"); // Sẽ được load thành ROLE_USER
            userRepository.save(user);
        }
    }
}
