package com.rsm.sample.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rsm.common.security.JwtManager;
import com.rsm.common.util.MessageReturn;
import com.rsm.common.vo.LoginVo;

@RestController
@RequestMapping("/auth")
public class LoginCtrl {

	private static Map<String, Object> result = new HashMap<String, Object>();
	private static String msg = null;

	@Autowired
	private JwtManager jwtManager;

	@Autowired
	private MessageReturn messageReturn;

	@RequestMapping(value = "login", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> login(@RequestBody LoginVo vo) {

		if (vo.getUserId().equals("admin") && vo.getPassword().equals("1234")) {
			result.put("authToken", "Bearer " + jwtManager.createToken());
		} else {
			result.put("authToken", null);
		}

		msg = "INFO_OK";

		return messageReturn.getRestResp(result, msg);
	}

}
