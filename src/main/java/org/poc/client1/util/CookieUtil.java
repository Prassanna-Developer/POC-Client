package org.poc.client1.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
	
	 @Value("${jwt.header}")
	 private String tokenHeader;
	 
	 
	 /**
	  * Get JWT Token from the request
	 * @param request
	 * @return
	 */
	public String getToken(HttpServletRequest request){
		 String token="";
		 Cookie[] cookies = request.getCookies();
		 if(cookies!=null)
		 {
			 for(Cookie cookie : cookies){
				 if(cookie.getName().equals(tokenHeader)){
					 token = cookie.getValue();
				 }
			 }
		 }
		 
		 return token;
	 }
	 
	 /**
	 * Create the cookie and add it to the response
	 */
	public Cookie createCookie(HttpServletResponse response,String value){
		 Cookie cookie = new Cookie(tokenHeader,value);
		 
		 // Cookie will be valid for only one hour in seconds
		 cookie.setMaxAge(3600);
		 
		 response.addCookie(cookie);
		 
		 return cookie;
	 }
	 
	 

}
