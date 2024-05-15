package com.example.secumix.security.store.model.entities;


import com.example.secumix.security.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "store")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private int storeId;

    @Column(name = "storename")
    private String storeName;

    @Column(name = "address")
    private String address;

    @Column(name = "phonenumber")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Column(name = "rate")
    private int rate;

    @Column(name = "image")
    private String image;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "storetypeid", foreignKey = @ForeignKey(name = "fk_store_storetype"))
    private StoreType storeType;

    @JsonIgnore
    @OneToMany(mappedBy = "store")
    @JsonManagedReference
    private List<ProductType> productType;

    @JsonIgnore
    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Product> productList;

    @Column
    private String emailmanager;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_stores",
            joinColumns = @JoinColumn(name = "store_id", referencedColumnName = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<User> users = new HashSet<>();

}