package com.example.secumix.entities;


import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity(name = "orderdetail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "orderdetail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderdetailid")
    private int orderDetailId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "shiptaken_at")
    private Date shipTakenAt;

    @Column(name = "shipped_at")
    private Date shippedAt;

    @Column(name = "cancel_at")
    private Date cancelAt;



    @Column(name = "quantity")
    @Min(value = 0)
    @Max(value = 20, message = "too many products")
    private int quantity;


    @Column(name = "procetotal")
    private long priceTotal;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeid", foreignKey = @ForeignKey(name = "fk_orderdetail_store"))
    private Store store;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid", foreignKey = @ForeignKey(name="fk_ordertail_product"))
    private Product product;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartid", foreignKey = @ForeignKey(name = "fk_orderdetail_cart"))
    private Cart cart;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", foreignKey = @ForeignKey(name = "fk_orderdetail_user"))
    private User user;

    private Integer shipperid=null;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderstatusid", foreignKey = @ForeignKey(name = "fk_orderdetail_orderstatus"))
    private OrderStatus orderStatus;


}