package com.example.secumix.entities;


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
    private long price;

    @Column(name = "productname")
    private String productName;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "status")
    private Integer status;
    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "description")
    private String description;

    @Column(name = "ban_reason")
    private String banReason;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "view")
    private int view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeid", foreignKey = @ForeignKey(name = "fk_product_store"))
    private Store store;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producttypeid", foreignKey = @ForeignKey(name = "fk_product_producttype"))
    private ProductType productType;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ProductImage> productImages;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<OrderDetail> orderDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ImportDetail> importDetails;

    public enum ChannelStatus {
        UNSELL(0),
        SELL(1),
        FORBIDDEN(2);
        private int value;
        ChannelStatus(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }



}