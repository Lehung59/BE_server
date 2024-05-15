package com.example.secumix.security.Oauth2;


import com.example.secumix.security.user.AuthenticationType;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class OAuthLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	@Lazy
	UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
		String oauth2ClientName = oauth2User.getOauth2ClientName();
		String username = oauth2User.getEmail();
		Optional<User> user = userService.FindByEmail(username);
		if (user.isEmpty()){
			User newUser = new User();
			newUser.setEmail(username);
			newUser.setAuthType(AuthenticationType.valueOf(oauth2ClientName.toUpperCase()));
			userService.SaveUser(user.get());
		}
		userService.updateAuthenticationType(username, oauth2ClientName.toUpperCase());

		super.onAuthenticationSuccess(request, response, authentication);

	}

}
