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
	public ResponseEntity<Map<String, Object>> sendPush() {

		result.clear();
//		ObjectMapper mapper = new ObjectMapper();
//
//		String jsonInString = null;

		try {
//			jsonInString = mapper.writeValueAsString(vo);

			HttpResponse<String> response = Unirest.post("http://10.211.55.6:8090/rsf-sample-backend/api/auth/send-push")
					  .header("Content-Type", "application/json")
					  .header("cache-control", "no-cache")
					  .header("Postman-Token", "4be5a575-b63f-4fba-a5c6-a3b5c479b77e")
					  .body("{ \"registration_ids\" : [ \"eYkYAcBUWDQ:APA91bFtN1dsRd3AcqJ_LC2iTZryF-XjLe-bX3hetUiYaSRDI8KulNotmPt9wmPub5OgBdn1ciNqi8nL8crFOiOeZXDquxSugn2jDeYhDBhlo524-SIaQ9gQ7NFlga5uKtoLvXv8MgQM\", \"fKd3ZCVxYIk:APA91bHuYu2EbMN3mZHh-_hJNDBESniVMJ-h4psk5FPPfNc876S2tecM6qLTG5rEzJGQX5Z67aI5Z7akB959GMDrHDx47kfh3wbi5StxHeDlTh0-DvdfNNweJY1umQKXHYaA_6OL2xLL\" ], \"notification\": { \"title\" : \"123456\", \"body\" : \"123456\" }, \"data\": { \"trimNo\" : \"234567\", \"model\" : \"890\", \"diverLoc\" : \"123\", \"destination\" : \"456\", \"swBzr\" : \"78\", \"rrack\" : \"90\" } }\n\n")
					  .asString();

			result.put("status", response.getStatus());

//		} catch (JsonProcessingException e) {
//			logger.error(e.getMessage());

		} catch (UnirestException e) {
			logger.error(e.getMessage());
		}

		// try {
		// HttpResponse<String> response =
		// Unirest.post("https://fcm.googleapis.com/fcm/send")
		// .header("Content-Type", "application/json")
		// .header("Authorization",
		// "key=AAAAhp3z7Co:APA91bHzwMP0_cx0qTcFlAUD9kN7xtTufNk_IG5QV7P9_BIJh7n9Qbg3p5rVk5G0ECbHUbRzTiJxSDSiyNGdeUzMKX_Vo6C5OBguvjAMpctFRBjSBZnf17caTlC4jKRszcOyofMc9swW5ZUIEAbhqf2Cg5iKH479VA")
		// .header("cache-control", "no-cache")
		// .body(jsonInString)
		// .asString();
		//
		// result.put("status", response.getStatus());
		//
		// } catch (UnirestException e) {
		// logger.error(e.getMessage());
		// }

		msg = "INFO_OK";

		return messageReturn.getRestResp(result, msg);
	}

}
