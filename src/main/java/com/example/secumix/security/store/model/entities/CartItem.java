package com.example.secumix.security.store.model.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "cartitem")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "cartitem")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartitemid")
    private int cartItemId;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "quantity")
    private int quantity;
    @Column(name = "pricetotal")
    private int pricetotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "cartid", foreignKey = @ForeignKey(name = "fk_cartitem_cart"))
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "productid", foreignKey = @ForeignKey(name = "fk_cartitem_product"))
    private Product product;

}