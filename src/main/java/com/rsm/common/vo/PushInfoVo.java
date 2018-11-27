package com.rsm.common.vo;

import java.io.Serializable;
import java.util.List;

public class PushInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> registration_ids;
	private NotiVo notification;
	private TrimInfoVo data;

	public List<String> getRegistration_ids() {
		return registration_ids;
	}

	public void setRegistration_ids(List<String> registration_ids) {
		this.registration_ids = registration_ids;
	}

	public NotiVo getNotification() {
		return notification;
	}

	public void setNotification(NotiVo notification) {
		this.notification = notification;
	}

	public TrimInfoVo getData() {
		return data;
	}

	public void setData(TrimInfoVo data) {
		this.data = data;
	}

}
