//package com.example.secumix.security.Oauth2;
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.Collection;
//import java.util.Map;
//
//public class CustomOAuth2User implements OAuth2User {
//    /* Lấy ra ClientName*/
//    @Getter
//    private String oauth2ClientName;
//	private OAuth2User oauth2User;
//
//	public CustomOAuth2User(OAuth2User oauth2User, String oauth2ClientName) {
//		this.oauth2User = oauth2User;
//		this.oauth2ClientName = oauth2ClientName;
//	}
//
//	@Override
//	public Map<String, Object> getAttributes() {
//		System.out.println("getAttributes()");
//		return oauth2User.getAttributes();
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		System.out.println("getAuthorities()");
//		return oauth2User.getAuthorities();
//	}
//    /* Lấy ra Email từ tài khoản Oauth*/
//	@Override
//	public String getName() {
//		System.out.println("day la mail: "+oauth2User.<String>getAttribute("email"));
//		return oauth2User.getAttribute("name");
//	}
//
//	public String getEmail() {
//		System.out.println("getEmail");
//
//		return oauth2User.<String>getAttribute("email");
//	}
//}
