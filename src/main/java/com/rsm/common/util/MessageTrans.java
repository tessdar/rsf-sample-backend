package com.rsm.common.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageTrans {

	private static final Map<String, Object> result = new HashMap<String, Object>();
	private static String msgRet = null;
	private String lang;

	@Autowired
	private MessageSource messageSource;

	public Map<String, Object> getMapLang(String msg) {
		
		if (this.lang.equals("en")) {
			msgRet = messageSource.getMessage(msg, null, Locale.US);
		} else {
			msgRet = messageSource.getMessage(msg, null, Locale.KOREAN);
		}

		result.put("message", msgRet);

		return result;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
}
