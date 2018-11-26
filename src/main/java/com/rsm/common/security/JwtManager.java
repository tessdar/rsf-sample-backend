package com.rsm.common.security;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtManager {
	private final String secretKey = "samplekey";

	public String createToken() {
		String token = null;

		final String issuer = "RSM";
		Date issuedAt = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 5);

		Date expiresAt = c.getTime();

		try {
			Algorithm algorithm = Algorithm.HMAC256(secretKey);
			token = JWT.create().withIssuer(issuer).withIssuedAt(issuedAt).withExpiresAt(expiresAt).sign(algorithm);
			return token;

		} catch (UnsupportedEncodingException e) {
			// UTF-8 encoding not supported
			return e.getMessage();

		} catch (JWTCreationException e) {
			// Invalid Signing configuration / Couldn't convert Claims.
			return e.getMessage();

		}

	}

	public String verifyToken(String token) throws UnsupportedEncodingException, JWTVerificationException {

		try {
			Algorithm algorithm = Algorithm.HMAC256(secretKey);
			JWTVerifier verifier = JWT.require(algorithm).build();

			DecodedJWT jwt = verifier.verify(token);
			return jwt.getIssuer();
		} catch (UnsupportedEncodingException e) {
			// UTF-8 encoding not supported
			throw e;
		} catch (JWTVerificationException e) {
			// Invalid signature/claims
			System.out.println(e.getMessage());
			throw e;
		}
	}

	public static void main(String[] args) {
		 JwtManager tm = new JwtManager();
		
		 System.out.println(tm.createToken());

//		try {
//			HttpResponse<String> response = Unirest.post("https://fcm.googleapis.com/fcm/send")
//					.header("Content-Type", "application/json")
//					.header("Authorization",
//							"key=AAAAhp3z7Co:APA91bHzwMP0_cx0qTcFlAUD9kN7xtTufNk_IG5QV7P9_BIJh7n9Qbg3p5rVk5G0ECbHUbRzTiJxSDSiyNGdeUzMKX_Vo6C5OBguvjAMpctFRBjSBZnf17caTlC4jKRszcOyofMc9swW5ZUIEAbhqf2Cg5iKH479VA")
//					.header("cache-control", "no-cache")
//					.body("{\n  \"to\":\n    \"eYkYAcBUWDQ:APA91bFtN1dsRd3AcqJ_LC2iTZryF-XjLe-bX3hetUiYaSRDI8KulNotmPt9wmPub5OgBdn1ciNqi8nL8crFOiOeZXDquxSugn2jDeYhDBhlo524-SIaQ9gQ7NFlga5uKtoLvXv8MgQM\",\n  \"notification\": {\n    \"title\": \"Test #14\",\n    \"body\": \"Rsf sample Test #14\"\n  }\n}")
//					.asString();
//
//			System.out.println(response.getStatus());
//
//		} catch (UnirestException e) {
//			e.printStackTrace();
//		}

	}

}
