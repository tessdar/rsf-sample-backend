package com.rsm.sample.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rsm.common.security.JwtManager;
import com.rsm.common.util.MessageReturn;
import com.rsm.common.vo.LoginVo;
import com.rsm.common.vo.PushInfoVo;

@RestController
@RequestMapping("/auth")
public class LoginCtrl {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(LoginCtrl.class);
	
	private static Map<String, Object> result = new HashMap<String, Object>();
	private static String msg = null;

	@Autowired
	private JwtManager jwtManager;

	@Autowired
	private MessageReturn messageReturn;

	@PostMapping(path = "/login", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> login(@RequestBody LoginVo vo) {

		result.clear();
		if (vo.getUserId().equals("admin") && vo.getPassword().equals("1234")) {
			result.put("authToken", "Bearer " + jwtManager.createToken());
		} else {
			result.put("authToken", null);
		}

		msg = "INFO_OK";

		return messageReturn.getRestResp(result, msg);
	}

	@PostMapping(path = "/send-push", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> sendPush(@RequestBody PushInfoVo vo) {

		result.clear();
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonInString = null;
		
		try {
			jsonInString = mapper.writeValueAsString(vo);
			
			HttpResponse<String> response = Unirest.post("https://fcm.googleapis.com/fcm/send")
					.header("Content-Type", "application/json")
					.header("Authorization",
							"key=AAAAhp3z7Co:APA91bHzwMP0_cx0qTcFlAUD9kN7xtTufNk_IG5QV7P9_BIJh7n9Qbg3p5rVk5G0ECbHUbRzTiJxSDSiyNGdeUzMKX_Vo6C5OBguvjAMpctFRBjSBZnf17caTlC4jKRszcOyofMc9swW5ZUIEAbhqf2Cg5iKH479VA")
					.header("cache-control", "no-cache")
					.body(jsonInString)
					.asString();

			result.put("status", response.getStatus());	
			
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());	
			
		} catch (UnirestException e) {
			logger.error(e.getMessage());	
		}
		
//		try {
//			HttpResponse<String> response = Unirest.post("https://fcm.googleapis.com/fcm/send")
//					.header("Content-Type", "application/json")
//					.header("Authorization",
//							"key=AAAAhp3z7Co:APA91bHzwMP0_cx0qTcFlAUD9kN7xtTufNk_IG5QV7P9_BIJh7n9Qbg3p5rVk5G0ECbHUbRzTiJxSDSiyNGdeUzMKX_Vo6C5OBguvjAMpctFRBjSBZnf17caTlC4jKRszcOyofMc9swW5ZUIEAbhqf2Cg5iKH479VA")
//					.header("cache-control", "no-cache")
//					.body(jsonInString)
//					.asString();
//
//			result.put("status", response.getStatus());	
//			
//		} catch (UnirestException e) {
//			logger.error(e.getMessage());	
//		}

		msg = "INFO_OK";

		return messageReturn.getRestResp(result, msg);
	}

}
