package com.rsm.common.security;

import java.io.IOException;
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

	public static void main(String[] args) throws IOException {
		JwtManager tm = new JwtManager();

		System.out.println(tm.createToken());
	}

}
