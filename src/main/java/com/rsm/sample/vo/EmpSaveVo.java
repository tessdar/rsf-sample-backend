package com.rsm.sample.vo;

import java.io.Serializable;
//import java.sql.Date;

public class EmpSaveVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long employeeId;

	private String firstName;

	private String lastName;

	private String phoneNumber;
	
	private String jobId;
	
	private Long departmentId;
	
	private Long managerId;
	
	private String email;
	
//	private Date hireDate;
	
	private Long _status;
	
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	public Date getHireDate() {
//		return hireDate;
//	}
//
//	public void setHireDate(Date hireDate) {
//		this.hireDate = hireDate;
//	}

	public Long get_status() {
		return _status;
	}

	public void set_status(Long _status) {
		this._status = _status;
	}

	@Override
	public String toString() {
		return "EmpSaveVo [employeeId=" + employeeId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", phoneNumber=" + phoneNumber + ", jobId=" + jobId + ", departmentId=" + departmentId
				+ ", managerId=" + managerId + ", email=" + email + ", _status=" + _status
				+ "]";
	}
	
}
