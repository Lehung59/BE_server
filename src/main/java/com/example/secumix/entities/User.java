package com.example.secumix.entities;



import com.example.secumix.notify.Notify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "_user")

public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Integer id;
  private String firstname;
  private String lastname;
  private String email;
  private String password;
  @Enumerated(EnumType.STRING)
  @Column(name = "auth_type")
  private AuthenticationType authType;

  @Enumerated(EnumType.STRING)
  private Role role;
  @Column(name = "enabled")
  private boolean enabled;

  @Column(name = "online_status")
  private boolean onlineStatus=false;

  @Column(name = "oauth2_id")
  private String oauth2Id;

  @Column(name = "facebook_id")
  private String facebookId;

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<Notify> notifies;

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<OrderDetail> orderDetails;

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
  private Set<Store> stores = new HashSet<>();


  public User() {

  }

  public User(String firstname, String lastname, String email, String password, AuthenticationType authType, Role role, boolean enabled, boolean onlineStatus) {
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
  public void addStore(Store store) {
    this.stores.add(store);
    store.getUsers().add(this);
  }

}
