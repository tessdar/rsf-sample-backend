package com.rsm.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.rsm.common.security.JwtManager;

public class LoginCheck extends HandlerInterceptorAdapter {

	@Autowired
	private JwtManager jwtManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// To avoid CORS
		if (request.getMethod().equals("OPTIONS")) {
			return true;
		}

		String authToken = request.getHeader("Authorization");
		
		if (authToken == null || !authToken.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;

		} else {
			String token = authToken.substring("Bearer".length()).trim();

			try {
				jwtManager.verifyToken(token);

			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}

			return true;
		}

	}

}
