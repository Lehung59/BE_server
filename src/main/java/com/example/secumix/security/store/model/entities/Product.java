package com.example.secumix.security.store.model.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity(name = "product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid")
    private int productId;

    @Column(name = "avatarproduct")
    private String avatarProduct;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "discount")
    private int discount;

    @Column(name = "price")
    private int price;

    @Column(name = "productname")
    private String productName;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "status")
    private int status;

    @Column(name = "description")
    private String description;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "view")
    private int view;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "storeid", foreignKey = @ForeignKey(name = "fk_product_store"))
    private Store store;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producttypeid", foreignKey = @ForeignKey(name = "fk_product_producttype"))
    private ProductType productType;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private Set<ProductImage> productImages;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private Set<OrderDetail> orderDetails;

    @JsonIgnore
    @OneToMany
    @JsonManagedReference
    private Set<ImportDetail> importDetails;

}