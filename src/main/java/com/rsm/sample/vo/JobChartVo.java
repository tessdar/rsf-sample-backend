package com.rsm.sample.vo;

import java.io.Serializable;

public class JobChartVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String jobId;
	
	private String jobTitle; 
	
	private Long jobCnt;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Long getJobCnt() {
		return jobCnt;
	}

	public void setJobCnt(Long jobCnt) {
		this.jobCnt = jobCnt;
	}

	@Override
	public String toString() {
		return "JobChartVo [jobId=" + jobId + ", jobTitle=" + jobTitle + ", jobCnt=" + jobCnt + "]";
	}
	
}
