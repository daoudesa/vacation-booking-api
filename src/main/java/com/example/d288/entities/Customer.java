package com.example.d288.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @NotEmpty(message = "First name is required")
    @Column(name = "customer_first_name")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    @Column(name = "customer_last_name")
    private String lastName;

    @NotEmpty(message = "Address is required")
    @Column(name = "address")
    private String address;

    @NotEmpty(message = "Postal code is required")
    @Column(name = "postal_code")
    @com.fasterxml.jackson.annotation.JsonProperty("postal_code")
    private String postalCode;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^[0-9\\-\\+\\s\\(\\)]*$", message = "Phone number format is invalid")
    @Column(name = "phone")
    private String phone;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "last_update")
    private Date lastUpdate;

    // MANY customers → ONE division
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "division_id")
    private Division division;

    // ONE customer → MANY carts
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Cart> carts = new HashSet<>();

    public void addCart(Cart cart) {
        if (cart != null) {
            if (carts == null) {
                carts = new HashSet<>();
            }
            carts.add(cart);
            cart.setCustomer(this);
        }
    }
}