package com.example.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    // Get user cart (Create if not exists)
    public Cart getUserCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setTotalAmount(0.0);
            return cartRepository.save(cart);
        });
    }

    // Add product to cart
    public Cart addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = getUserCart(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(product.getPrice() * quantity);

        cart.getCartItems().add(cartItem);
        cart.setTotalAmount(cart.getTotalAmount() + cartItem.getPrice());

        cartItemRepository.save(cartItem);
        return cartRepository.save(cart);
    }

    // Remove product from cart
    public Cart removeProductFromCart(Long userId, Long productId) {
        Cart cart = getUserCart(userId);
        List<CartItem> items = cart.getCartItems();

        Optional<CartItem> cartItemOptional = items.stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cart.setTotalAmount(cart.getTotalAmount() - cartItem.getPrice());
            items.remove(cartItem);
            cartItemRepository.delete(cartItem);
        }

        return cartRepository.save(cart);
    }
}
