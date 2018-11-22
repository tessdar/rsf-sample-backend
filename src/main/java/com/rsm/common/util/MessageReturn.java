package com.rsm.common.util;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MessageReturn {

	public ResponseEntity<Map<String, Object>> getRestResp(Map<String, Object> result, String msg) {

		if (msg == null) {
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.INTERNAL_SERVER_ERROR);

		} else {

			if (msg.substring(0, 3).equals("ERR")) {
				return new ResponseEntity<Map<String, Object>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
			}
		}

	}

	public <T> ResponseEntity<List<T>> getRestRespList(List<T> result) {

		if (result.isEmpty()) {
			return new ResponseEntity<List<T>>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<T>>(result, HttpStatus.OK);
		}
	}

}
