package com.rsm.sample.service;

import java.util.List;

import com.rsm.sample.vo.DepChartVo;
import com.rsm.sample.vo.DepListVo;
import com.rsm.sample.vo.EmpListVo;
import com.rsm.sample.vo.EmpSaveVo;
import com.rsm.sample.vo.JobChartVo;
import com.rsm.sample.vo.JobListVo;

public interface SampleService {
	
	public List<EmpListVo> getEmpList(String departmentId);
	
	public String setEmp(List<EmpSaveVo> vos) throws Exception;
	
	public String delEmp(List<EmpSaveVo> vos) throws Exception;
	
	public String insEmp(List<EmpSaveVo> vos) throws Exception;
	
	public List<DepListVo> getDepList();
	
	public List<JobListVo> getJobList();
	
	public List<DepChartVo> getDepChart();
	
	public List<JobChartVo> getJobChart();	
	
	public void scheRefreshCache();
	
}
