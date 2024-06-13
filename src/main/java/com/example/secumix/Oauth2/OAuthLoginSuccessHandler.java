package com.example.secumix.Oauth2;


import com.example.secumix.entities.AuthenticationType;
import com.example.secumix.entities.User;
import com.example.secumix.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

		String accessToken =oauth2User.getAccessToken();


		String targetUrl = determineTargetUrl(request, response, authentication);
		getRedirectStrategy().sendRedirect(request, response, targetUrl+accessToken);

	}
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		return "http://localhost:5173/login?token=";
	}

}