package com.example.secumix.security.auth;


import com.example.secumix.security.auth.rule.PasswordMatches;
import com.example.secumix.security.auth.rule.ValidEmail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  private String firstname;
  private String lastname;
  @ValidEmail
  private String email;
  private String password;

}
