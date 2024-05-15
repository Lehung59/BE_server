package com.example.secumix.security.Oauth2;


import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		User userDetails = (User) authentication.getPrincipal();
		userService.updateAuthenticationType(userDetails.getUsername(), "DATABASE");
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
