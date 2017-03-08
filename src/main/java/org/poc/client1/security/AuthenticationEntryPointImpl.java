package org.poc.client1.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.poc.client1.util.CookieUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, InitializingBean {
	

	private static final Log logger = LogFactory.getLog(AuthenticationEntryPointImpl.class);

	@Value("${sso.app.login.url}")
	private String loginFormUrl;
	
	@Value("${sso.app.client.url}")
	private String clientURL;
	
	 @Autowired
	 private CookieUtil cookieUtil;

	/**
	 * Performs the redirect (or forward) to the login form URL.
	 */
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		String  authToken = cookieUtil.getToken(request);
		if(authToken == null || authToken.isEmpty()){
			response.sendRedirect(loginFormUrl.toString() + "?clientURL=" +clientURL);
		}
		else{
			authorizeToken(request, response, authToken);
		}
		
	}

	private void authorizeToken(HttpServletRequest request, HttpServletResponse response, String authToken)
			throws IOException {
		final String requestedURL = request.getRequestURL().toString();
		response.sendRedirect(requestedURL + "/authorize?jwtToken=" + authToken);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("AuthenticationEntryPointImpl.afterPropertiesSet()");
	}

}