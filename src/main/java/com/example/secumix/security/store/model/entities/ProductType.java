package com.example.secumix.security.store.model.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "producttype")
@Data
@Getter
@Setter
@Table(name = "producttype")
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producttypeid")
    private int productTypeId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "producttypeimg")
    private String productTypeImg;

    @Column(name = "producttypename")
    private String productTypeName;

    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "productType")
    @JsonManagedReference
    private Set<Product> products;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "storeid",foreignKey = @ForeignKey(name = "fk_store_producttype"))
    private Store store;

}