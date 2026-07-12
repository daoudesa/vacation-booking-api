package com.example.d288.services;

import com.example.d288.dao.CustomerRepository;
import com.example.d288.entities.Cart;
import com.example.d288.entities.CartItem;
import com.example.d288.entities.Customer;
import com.example.d288.entities.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        Cart cart = purchase.getCart();
        Set<CartItem> cartItems = purchase.getCartItems();
        Customer incomingCustomer = purchase.getCustomer();

        // Angular only sends a partial customer object (name, address, phone, id).
        // Fetch the real, complete customer record so we don't overwrite
        // existing fields like createDate/division with nulls.
        Customer customer = customerRepository.findById(incomingCustomer.getId())
                .orElse(incomingCustomer);

        // Angular sends placeholder ids of 0 for new carts/cart items;
        // force null so Hibernate always treats them as new inserts
        cart.setId(null);
        cartItems.forEach(cartItem -> cartItem.setId(null));

        // set timestamps since Angular doesn't send them
        Date now = new Date();
        cart.setCreateDate(now);
        cart.setLastUpdate(now);
        cartItems.forEach(cartItem -> {
            cartItem.setCreateDate(now);
            cartItem.setLastUpdate(now);
        });

        // generate a unique tracking number for this order
        String orderTrackingNumber = generateOrderTrackingNumber();
        cart.setOrderTrackingNumber(orderTrackingNumber);
        cart.setStatus(StatusType.ordered);

        // link each cart item back to the cart (maintain both sides of the relationship)
        cartItems.forEach(cart::addCartItem);

        // link the cart back to the customer (maintain both sides of the relationship)
        customer.addCart(cart);

        // saving the customer will cascade and save the cart and cart items too,
        // because Customer -> Cart is set up with CascadeType.ALL
        customerRepository.save(customer);

        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
        // generate a random UUID to use as a unique tracking number
        return UUID.randomUUID().toString();
    }
}