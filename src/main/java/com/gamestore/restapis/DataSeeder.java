package com.gamestore.restapis;

import com.gamestore.restapis.entities.Category;
import com.gamestore.restapis.entities.Product;
import com.gamestore.restapis.entities.User;
import com.gamestore.restapis.repositories.CategoryRepository;
import com.gamestore.restapis.repositories.ProductRepository;
import com.gamestore.restapis.repositories.UserRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Faker faker = new Faker();

    public DataSeeder(
            UserRepository userRepository,
            ProductRepository productRepository,
            CategoryRepository categoryRepository
    ) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedProducts();
    }

    private void seedUsers() {
        if (userRepository.count() > 0) {
            return;
        }

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();

            users.add(User.builder()
                    .name(firstName + " " + lastName)
                    .email(faker.internet().emailAddress(firstName.toLowerCase() + "." + lastName.toLowerCase()))
                    .password(faker.internet().password(10, 20))
                    .build());
        }

        userRepository.saveAll(users);
    }

    private void seedProducts() {
        if (productRepository.count() > 0) {
            return;
        }

        List<Category> categories = getOrCreateCategories();
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            products.add(Product.builder()
                    .name(faker.commerce().productName())
                    .description(faker.lorem().sentence())
                    .price(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 150)))
                    .category(categories.get(i % categories.size()))
                    .build());
        }

        productRepository.saveAll(products);
    }

    private List<Category> getOrCreateCategories() {
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);

        if (!categories.isEmpty()) {
            return categories;
        }

        categories.add(new Category("Consoles"));
        categories.add(new Category("Games"));
        categories.add(new Category("Accessories"));
        categories.add(new Category("Collectibles"));

        List<Category> savedCategories = new ArrayList<>();
        categoryRepository.saveAll(categories).forEach(savedCategories::add);
        return savedCategories;
    }
}
