package com.rsm.sample.controller;

import java.io.IOException;
//import java.io.FileInputStream;
//import java.io.IOException;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rsm.common.security.JwtManager;
import com.rsm.common.util.MessageReturn;
import com.rsm.common.util.MessageTrans;
import com.rsm.common.vo.LoginVo;
import com.rsm.common.vo.PushInfoVo;
import com.rsm.sample.service.SampleService;

@RestController
@RequestMapping("/auth")
public class LoginCtrl {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(LoginCtrl.class);

	private static Map<String, Object> result = new HashMap<String, Object>();
	private static String msg = null;

	@Autowired
	private SampleService service;

	@Autowired
	private JwtManager jwtManager;
	
	@Autowired
	private MessageTrans messageTrans;
	
	@Autowired
	private MessageReturn messageReturn;

	@PostMapping(path = "/login", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> login(@RequestBody LoginVo vo) {

		result.clear();
		try {			
			msg = service.chkLogin(vo);
			result.put("authToken", "Bearer " + jwtManager.createToken());
		} catch (Exception e) {
			msg = e.getMessage();
			result.put("authToken", null);
		}

		result = messageTrans.getMapLang(msg);

		return messageReturn.getRestResp(result, msg);

		// ----------------

		
//		try {
//			FileInputStream serviceAccount = new FileInputStream(
//					"/Users/kimwonchul/workspace-sts/rsf-sample-backend/src/main/resources/asftest-3d5e2-firebase-adminsdk-8wlse-80f27cc5c1.json");
//
//			
//			FirebaseOptions options = new FirebaseOptions.Builder()
//					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
//					.setDatabaseUrl("https://asftest-3d5e2.firebaseio.com").build();
//			
//			FirebaseApp.initializeApp(options);
//			
//			// As an admin, the app has access to read and write all data, regardless of
//			// Security Rules
//			DatabaseReference ref = FirebaseDatabase.getInstance().getReference("members/C0000072/K0100020");
//			
//			System.out.println(ref);  
//			
//			ref.addListenerForSingleValueEvent(new ValueEventListener() {
//				@Override
//				public void onDataChange(DataSnapshot dataSnapshot) {
//					String roomId = dataSnapshot.getValue(String.class);
//					
//					System.out.println(roomId);  
//					
//				}
//
//				@Override
//				public void onCancelled(DatabaseError error) {
//				}
//			});	
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	
		// ----------------
		
	}

	/**
	 * Firebase Push 전송 및 Realtime Database로 값을 입력하는 API
	 * @param vo
	 * @return
	 */
	@PostMapping(path = "/send-push", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> sendPush(@RequestBody PushInfoVo vo) {

		result.clear();
		ObjectMapper mapper = new ObjectMapper();

		String jsonInString = null, jsonDbString = null;

		try {
			
			jsonInString = mapper.writeValueAsString(vo);
			JsonNode rootNode = mapper.readTree(jsonInString);
			
			JsonNode locatedNode = rootNode.path("data");
			jsonDbString = locatedNode.toString();
			
			HttpResponse<String> response_push = Unirest.post("https://fcm.googleapis.com/fcm/send")
					.header("Content-Type", "application/json")
					.header("Authorization",
							"key=AAAAhp3z7Co:APA91bHzwMP0_cx0qTcFlAUD9kN7xtTufNk_IG5QV7P9_BIJh7n9Qbg3p5rVk5G0ECbHUbRzTiJxSDSiyNGdeUzMKX_Vo6C5OBguvjAMpctFRBjSBZnf17caTlC4jKRszcOyofMc9swW5ZUIEAbhqf2Cg5iKH479VA")
					.header("cache-control", "no-cache")
					.body(jsonInString)
					.asString();

			HttpResponse<String> response_db = Unirest.post("https://asftest-3d5e2.firebaseio.com/rsm/plant/swi.json")
					  .header("Content-Type", "application/json")
					  .header("cache-control", "no-cache")
					  .body(jsonDbString)
					  .asString();
			
			result.put("status_push", response_push.getStatus());
			result.put("status_db", response_db.getStatus());

		} catch (IOException e) {
			logger.error(e.getMessage());

		} catch (UnirestException e) {
			logger.error(e.getMessage());
		}

		msg = "INFO_OK";

		return messageReturn.getRestResp(result, msg);
	}
	
	/**
	 * Firebase Push 전송 및 Realtime Database로 값을 입력하는 API
	 * @param vo
	 * @return
	 */
	@PostMapping(path = "/call-emerg", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> callEmergency(@RequestBody PushInfoVo vo) {

		result.clear();
		ObjectMapper mapper = new ObjectMapper();

		String jsonInString = null, jsonDbString = null;

		try {
			
			jsonInString = mapper.writeValueAsString(vo);
			JsonNode rootNode = mapper.readTree(jsonInString);
			
			JsonNode locatedNode = rootNode.path("data");
			jsonDbString = locatedNode.toString();
			
			HttpResponse<String> response_push = Unirest.post("https://fcm.googleapis.com/fcm/send")
					.header("Content-Type", "application/json")
					.header("Authorization",
							"key=AAAAhp3z7Co:APA91bHzwMP0_cx0qTcFlAUD9kN7xtTufNk_IG5QV7P9_BIJh7n9Qbg3p5rVk5G0ECbHUbRzTiJxSDSiyNGdeUzMKX_Vo6C5OBguvjAMpctFRBjSBZnf17caTlC4jKRszcOyofMc9swW5ZUIEAbhqf2Cg5iKH479VA")
					.header("cache-control", "no-cache")
					.body(jsonInString)
					.asString();

			HttpResponse<String> response_db = Unirest.post("https://asftest-3d5e2.firebaseio.com/rsm/plant/call.json")
					  .header("Content-Type", "application/json")
					  .header("cache-control", "no-cache")
					  .body(jsonDbString)
					  .asString();
			
			result.put("status_push", response_push.getStatus());
			result.put("status_db", response_db.getStatus());

		} catch (IOException e) {
			logger.error(e.getMessage());

		} catch (UnirestException e) {
			logger.error(e.getMessage());
		}

		msg = "INFO_OK";

		return messageReturn.getRestResp(result, msg);
	}

}
