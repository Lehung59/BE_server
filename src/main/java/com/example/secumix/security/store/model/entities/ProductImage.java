package com.example.secumix.security.store.model.entities;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "productimage")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "productimage")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productimageid")
    private int productImageId;

    @Column(name = "imageproduct")
    private String imageProduct;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "title")
    private String title;

    @Column(name = "status")
    private int status;



    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product product;

}