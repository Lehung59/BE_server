package com.example.secumix.security.user;


import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.auth.AuthenticationResponse;

import com.example.secumix.security.auth.AuthenticationService;
import com.example.secumix.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;


    /* Đổi mật khẩu*/
    public void changePassword(ChangePasswordRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = repository.findByEmail(auth.getName()).get();


        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }
    /* Tìm kiếm người dùng theo email */
    public Optional<User> FindByEmail(@Param("email") String email) {
        Optional<User> result = repository.findByEmail(email.trim().toLowerCase());
        return result;
    }
    /* Lưu người dùng*/
    public void SaveUser (User user){
       Optional<User> result = repository.findByEmail(user.getEmail());
       if (result.isPresent()){
           result.get().setFirstname(user.getFirstname());
           result.get().setLastname(user.getLastname());
           result.get().setEnabled(user.isEnabled());
           result.get().setTokens(user.getTokens());
           result.get().setRole(user.getRole());
           repository.save(user);
       }
    }
    /* Cập nhận kiểu đăng ký phân quyền*/
    public void updateAuthenticationType(String username, String oauth2ClientName) {
        AuthenticationType authType = AuthenticationType.valueOf(oauth2ClientName.toUpperCase());
        System.out.println("Updated user's authentication type to " + authType +" and email :"+ username);
        repository.updateAuthenticationType(username,authType);

    }
    public User findById(int userID){
        return repository.findById(userID).get();
    }

}

