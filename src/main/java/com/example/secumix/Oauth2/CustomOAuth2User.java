package com.example.secumix.Oauth2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
	private String oauth2ClientName;
	private OAuth2User oauth2User;
	private String accessToken;



	public CustomOAuth2User(OAuth2User oauth2User, String oauth2ClientName, String accessToken) {
		this.oauth2User = oauth2User;
		this.oauth2ClientName = oauth2ClientName;
		this.accessToken=accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oauth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oauth2User.getAuthorities();
	}
	/* Lấy ra Email từ tài khoản Oauth*/
	@Override
	public String getName() {
		System.out.println(oauth2User.<String>getAttribute("email"));
		return oauth2User.getAttribute("name");
	}

	public String getEmail() {
		return oauth2User.<String>getAttribute("email");
	}
	/* Lấy ra ClientName*/
	public String getOauth2ClientName() {
		return this.oauth2ClientName;
	}
}