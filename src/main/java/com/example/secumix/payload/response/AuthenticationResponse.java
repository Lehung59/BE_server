package com.example.secumix.payload.response;

import com.example.secumix.entities.Role;
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
  private Integer storeId;
  @JsonProperty("accesstoken")
  private String accessToken;
  @JsonProperty("refreshtoken")
  private String refreshToken;

}
