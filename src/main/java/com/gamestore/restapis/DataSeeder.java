package com.gamestore.restapis;

import com.gamestore.restapis.entities.Category;
import com.gamestore.restapis.entities.Product;
import com.gamestore.restapis.entities.Role;
import com.gamestore.restapis.entities.User;
import com.gamestore.restapis.repositories.CategoryRepository;
import com.gamestore.restapis.repositories.ProductRepository;
import com.gamestore.restapis.repositories.UserRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker();
    private static final List<String> GAME_CATEGORIES = List.of(
            "Action",
            "RPG",
            "Sports",
            "Strategy",
            "Adventure",
            "Racing",
            "Horror",
            "Simulation"
    );

    private static final List<String> TITLE_PREFIXES = List.of(
            "Neon",
            "Shadow",
            "Cyber",
            "Iron",
            "Star",
            "Dark",
            "Pixel",
            "Ghost",
            "Solar",
            "Crimson"
    );

    private static final List<String> TITLE_SUFFIXES = List.of(
            "Strike",
            "Legends",
            "Frontier",
            "Arena",
            "Drift",
            "Quest",
            "Tactics",
            "Protocol",
            "Rivals",
            "Odyssey"
    );

    private static final List<String> PRODUCT_IMAGES = List.of(
            "/images/products/ghost-of-yotei.png",
            "/images/products/ac-shadows.png",
            "/images/products/war_thunder.png",
            "/images/products/rdr2.png",
            "/images/products/fortnite.png",
            "/images/products/uncharted.png",
            "/images/products/nfs.png",
            "/images/products/fifa.png",
            "/images/products/cod.png"
    );

    public DataSeeder(
            UserRepository userRepository,
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedProducts();
    }

    private void seedUsers() {
        if (userRepository.count() > 0) {
            assignMissingUserRoles();
            return;
        }

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();

            users.add(User.builder()
                    .name(firstName + " " + lastName)
                    .email(faker.internet().emailAddress(firstName.toLowerCase() + "." + lastName.toLowerCase()))
                    .password(passwordEncoder.encode(faker.internet().password(10, 20)))
                    .role(Role.USER)
                    .build());
        }

        userRepository.saveAll(users);
    }

    private void assignMissingUserRoles() {
        var usersWithoutRoles = userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == null)
                .peek(user -> user.setRole(Role.USER))
                .toList();

        if (!usersWithoutRoles.isEmpty()) {
            userRepository.saveAll(usersWithoutRoles);
        }
    }

    private void seedProducts() {
        Map<String, Product> existingProductsByName = productRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Product::getName, product -> product, (first, second) -> first));

        Map<String, Category> categoriesByName = getOrCreateCategories()
                .stream()
                .collect(Collectors.toMap(Category::getName, category -> category));

        var products = generateGameSeeds()
                .stream()
                .map(game -> {
                    var product = existingProductsByName.get(game.name());
                    if (product == null) {
                        return Product.builder()
                                .name(game.name())
                                .description(game.description())
                                .price(game.price())
                                .imageUrl(game.imageUrl())
                                .category(categoriesByName.get(game.category()))
                                .build();
                    }

                    product.setDescription(game.description());
                    product.setPrice(game.price());
                    product.setImageUrl(game.imageUrl());
                    product.setCategory(categoriesByName.get(game.category()));
                    return product;
                })
                .toList();

        productRepository.saveAll(products);
    }

    private List<Category> getOrCreateCategories() {
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);

        var existingCategoryNames = categories
                .stream()
                .map(Category::getName)
                .collect(Collectors.toSet());

        List<Category> missingCategories = GAME_CATEGORIES
                .stream()
                .filter(categoryName -> !existingCategoryNames.contains(categoryName))
                .map(Category::new)
                .toList();

        if (missingCategories.isEmpty()) {
            return categories;
        }

        List<Category> savedCategories = new ArrayList<>();
        categoryRepository.saveAll(missingCategories).forEach(savedCategories::add);

        categories.addAll(savedCategories);
        return categories;
    }

    private List<GameSeed> generateGameSeeds() {
        List<GameSeed> games = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            String category = GAME_CATEGORIES.get(i % GAME_CATEGORIES.size());
            String prefix = TITLE_PREFIXES.get(i % TITLE_PREFIXES.size());
            String suffix = TITLE_SUFFIXES.get((i / TITLE_PREFIXES.size() + i) % TITLE_SUFFIXES.size());
            BigDecimal price = BigDecimal.valueOf(19.99 + (i % 6) * 10);

            games.add(new GameSeed(
                    prefix + " " + suffix + " " + (i + 1),
                    "A " + category.toLowerCase() + " game with fast progression and replayable challenges.",
                    price,
                    category,
                    PRODUCT_IMAGES.get(i % PRODUCT_IMAGES.size())
            ));
        }

        return games;
    }

    private record GameSeed(
            String name,
            String description,
            BigDecimal price,
            String category,
            String imageUrl
    ) {
    }
}
