package org.poc.client1.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.poc.client1.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WelcomeController {

	@Autowired
	private CookieUtil cookieUtil;

	@Value("${sso.app.login.url}")
	private String loginFormUrl;

	@Value("${sso.app.client.url}")
	private String clientURL;

	@RequestMapping(value = "/authorize", method = RequestMethod.GET)
	public ModelAndView authorize(@RequestParam String jwtToken) {
		System.out.println("WelcomeController.welcome() > " + jwtToken);
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
