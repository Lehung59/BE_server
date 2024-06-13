package com.example.secumix.entities;


import lombok.*;

import javax.persistence.*;

@Entity(name = "storetype")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "storetype")
public class StoreType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storeid")
    private int storeTypeId;

    private String storeTypeName;



}