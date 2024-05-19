package com.example.secumix.security.store.model.request;

import com.example.secumix.security.store.model.entities.Product;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportEditRequest {
    private int importDetailId;
    private long price;
    private long priceTotal;
    private int quantity;
}
