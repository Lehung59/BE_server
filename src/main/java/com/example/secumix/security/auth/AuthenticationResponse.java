package com.example.secumix.security.auth;

import com.example.secumix.security.user.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  private int userId;
  private String firstname;
  private String lastname;
  private String email;
  private Role role;

  @JsonProperty("accesstoken")
  private String accessToken;
  @JsonProperty("refreshtoken")
  private String refreshToken;

}
