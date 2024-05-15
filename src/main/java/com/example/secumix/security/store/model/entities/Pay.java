package com.example.secumix.security.store.model.entities;


import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "pay")
@Table(name = "pay")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payid")
    private int payId;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "originalprice")
    private int originalPrice;

    @Column(name = "userid")
    private int userId;


    @Column(name = "paymentmethod")
    private String paymentMethod;

    @Column(name = "status")
    private int status;

    @Column(name = "voucherid")
    private Integer voucherId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "orderdetailid", foreignKey = @ForeignKey(name = "fk_pay_orderdetail"))
    private OrderDetail orderDetail;


}