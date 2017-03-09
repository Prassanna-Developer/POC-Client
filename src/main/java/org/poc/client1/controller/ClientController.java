package org.poc.client1.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.poc.client1.util.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ClientController {

	@Autowired
	private CookieUtil cookieUtil;
	
	private final Logger logger = LoggerFactory.getLogger(ClientController.class);

	@Value("${sso.app.login.url}")
	private String loginFormUrl;

	@Value("${sso.app.client.url}")
	private String clientURL;

	@RequestMapping(value = "/authorize", method = RequestMethod.GET)
	public ModelAndView authorize(@RequestParam String jwtToken) {
		logger.info("Token {} from the request",jwtToken);
		return new ModelAndView("display.html");
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authToken = cookieUtil.getToken(request);
		if (authToken == null || authToken.isEmpty()) {
			response.sendRedirect(loginFormUrl.toString() + "?clientURL=" + clientURL);
		} else {
			final String requestedURL = request.getRequestURL().toString();
			response.sendRedirect(requestedURL + "authorize?jwtToken=" + authToken);
		}
		return new ModelAndView("display.html");
	}

}
