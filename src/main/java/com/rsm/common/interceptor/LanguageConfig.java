package com.rsm.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.rsm.common.util.MessageTrans;

public class LanguageConfig extends HandlerInterceptorAdapter {

	@Autowired
	private MessageTrans messageTrans;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
			
		if (request.getHeader("Language") == null) {
			messageTrans.setLang("ko");
		} else {
			messageTrans.setLang(request.getHeader("Language"));
		}

		return true;
	}

}
