package com.example.secumix.entities;


import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "importdt")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "importdt")
public class ImportDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "importdetailid")
    private int importDetailId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "price")
    private long price;

    @Column(name = "pricetotal")
    private long priceTotal;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product product;


}