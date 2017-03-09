package org.poc.client1.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.poc.client1.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthenticationTokenFilterImpl extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;
    
    @Autowired
    private CookieUtil cookieUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    		String authToken = getTokenFromCookie(request, response);
    	
			String username = jwtTokenUtil.getUsernameFromToken(authToken);
			logger.info("checking authentication for user " + username);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				// It is not compelling necessary to load the use details from the database. You could also store the information
				// in the token and read it from it. It's up to you ;)
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

				// For simple validation it is completely sufficient to just check the token integrity. You don't have to call
				// the database compellingly. Again it's up to you ;)
				if (jwtTokenUtil.validateToken(authToken, userDetails)) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					logger.info("authenticated user " + username + ", setting security context");
					SecurityContextHolder.getContext().setAuthentication(authentication);
					
				}
			} 
		chain.doFilter(request, response);
    }

	private String getTokenFromCookie(HttpServletRequest request, HttpServletResponse response) {
		String  authToken = cookieUtil.getToken(request);
    	String queryString = ((HttpServletRequest) request).getQueryString();
    	if((authToken == null || authToken.isEmpty()) && queryString!=null) {
    		authToken = queryString.substring((queryString).lastIndexOf('=') + 1);
    		cookieUtil.createCookie(response, authToken);
    	}
		return authToken;
	}
    
}