package br.com.iff.marketplace.cart.service;

import br.com.iff.marketplace.cart.dto.CartResponseDTO;
import br.com.iff.marketplace.cart.repository.ShoppingCartRepository;
import br.com.iff.marketplace.cart.ShoppingCart;
import br.com.iff.marketplace.cart.ShoppingCartItem;
import br.com.iff.marketplace.product.repository.ProductVariationRepository;
import br.com.iff.marketplace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import br.com.iff.marketplace.cart.dto.AddItemToCartDTO;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.product.ProductVariation;
import org.springframework.transaction.annotation.Transactional;
import br.com.iff.marketplace.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductVariationRepository productVariationRepository;
    private final UserRepository userRepository;

    public CartResponseDTO getCartForUser(Long customerId) {

        ShoppingCart cart = getOrCreateCart(customerId);

        BigDecimal totalAmount = cart.getItems().stream()
                .map(item -> item.getVariation().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartResponseDTO responseDTO = new CartResponseDTO(cart);

        responseDTO.setTotalAmount(totalAmount);

        return responseDTO;
    }

    private ShoppingCart getOrCreateCart(Long customerId){
        return shoppingCartRepository.findByUserId(customerId)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    User customer = userRepository.findById(customerId)
                            .orElseThrow(() -> new NotFoundException("Usuário com ID " + customerId + " não encontrado"));
                    newCart.setUser(customer);
                    return shoppingCartRepository.save(newCart);
                });
    }

    @Transactional
    public CartResponseDTO addItemToCart(
            Long customerId,
            AddItemToCartDTO itemDTO) {

        ShoppingCart cart = getOrCreateCart(customerId);

        ProductVariation productVariation = productVariationRepository.findById(itemDTO.getVariationId())
                .orElseThrow(() -> new NotFoundException("Variação de produto com ID " + itemDTO.getVariationId() + " não encontrada!"));

        if (productVariation.getStock() < itemDTO.getQuantity()) {
            throw new IllegalStateException("Estoque insuficiente para a quantidade solicitada. Disponível: " + productVariation.getStock());
        }

        Optional<ShoppingCartItem> existingItem  = cart.getItems().stream()
                .filter(item -> item.getVariation().getId().equals(itemDTO.getVariationId()))
                .findFirst();

        if (existingItem.isPresent()) {
            ShoppingCartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + itemDTO.getQuantity();

            if (productVariation.getStock() < newQuantity) {
                throw new IllegalStateException("Estoque insuficiente para adicionar mais unidades. Quantidade no carrinho: " + item.getQuantity() + ", Disponível: " + productVariation.getStock());
            }
            item.setQuantity(newQuantity);
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setCart(cart);
            newItem.setVariation(productVariation);
            newItem.setQuantity(itemDTO.getQuantity());
            cart.getItems().add(newItem);
        }

        shoppingCartRepository.save(cart);
        return getCartForUser(customerId);
    }

    @Transactional
    public CartResponseDTO removeItemFromCart(Long customerId, Long itemId) {

        ShoppingCart cart = getOrCreateCart(customerId);

        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(itemId));

        if (!removed) {
            throw new RuntimeException("Item não encontrado no carrinho!");
        }

        shoppingCartRepository.save(cart);
        return getCartForUser(customerId);
    }

    @Transactional
    public CartResponseDTO updateItemQuantity(Long customerId, Long itemId, int newQuantity) {

        if (newQuantity <= 0) {return removeItemFromCart(customerId, itemId);}

        ShoppingCart cart = getOrCreateCart(customerId);

        ShoppingCartItem itemToUpdate = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item de Id " + itemId + " não encontrado no carrinho!"));

        if (itemToUpdate.getVariation().getStock() < newQuantity) {
            throw new RuntimeException("Estoque insuficiente. Disponível: " + itemToUpdate.getVariation().getStock());
        }

        itemToUpdate.setQuantity(newQuantity);

        shoppingCartRepository.save(cart);
        return getCartForUser(customerId);
    }

}