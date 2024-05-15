package com.example.secumix.security.userprofile;

import com.example.secumix.security.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Entity(name = "profile")
@Table(name = "profile")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profileId;
    private String firstname;
    private String lastname;
    private String email;
    @Column(name = "address")
    private String address;
    @Column(name = "phonenumber")
    @Pattern(regexp = "0\\d{9}", message = "Invalid phone number format. Should start with 0 and have 10 digits.")
    private String phoneNumber;
    @Column(name = "socialcontact")
    private String socialContact;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "createdAt")
    private Date createdAt;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID",foreignKey = @ForeignKey(name = "fk_user_profiledetail"))
    private User user;

}
