package com.rsm.sample.vo;

import java.io.Serializable;

public class DepChartVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long departmentId;
	
	private String departmentName; 
	
	private Long depCnt;

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getDepCnt() {
		return depCnt;
	}

	public void setDepCnt(Long depCnt) {
		this.depCnt = depCnt;
	}

	@Override
	public String toString() {
		return "DepChartVo [departmentId=" + departmentId + ", departmentName=" + departmentName + ", depCnt=" + depCnt
				+ "]";
	}

}
