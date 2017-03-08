package org.poc.client1.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
	
	 @Value("${jwt.header}")
	 private String tokenHeader;
	 
	 
	 public String getToken(HttpServletRequest request){
		 String token="";
		 Cookie[] cookies = request.getCookies();
		 
		 for(Cookie cookie : cookies){
			 if(cookie.getName().equals(tokenHeader)){
				 token = cookie.getValue();
			 }
		 }
		 
		 return token;
	 }
	 
	 

}
