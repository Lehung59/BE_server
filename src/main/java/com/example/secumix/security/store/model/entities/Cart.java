package com.example.secumix.security.store.model.entities;


import com.example.secumix.security.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "cart")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartid")
    private int cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid",  foreignKey = @ForeignKey(name = "fk_cart_user"))
    private User user;

    @OneToMany(mappedBy = "cart")
    @JsonIgnore
    private Set<CartItem> cartItems;

}