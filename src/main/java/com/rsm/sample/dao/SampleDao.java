package com.rsm.sample.dao;

import java.util.List;

import com.rsm.sample.vo.DepChartVo;
import com.rsm.sample.vo.DepListVo;
import com.rsm.sample.vo.EmpListVo;
import com.rsm.sample.vo.EmpSaveVo;
import com.rsm.sample.vo.JobChartVo;
import com.rsm.sample.vo.JobListVo;

public interface SampleDao {
	
	public List<EmpListVo> getEmpList(String departmentId);
	
	public int setEmp(EmpSaveVo empSaveDto) throws Exception;
	
	public int delEmp(Long employeeId) throws Exception;
	
	public int insEmp(EmpSaveVo empSaveDto) throws Exception;
	
	public List<DepListVo> getDepList();
	
	public List<JobListVo> getJobList();
	
	public List<DepChartVo> getDepChart();
	
	public List<JobChartVo> getJobChart();

}
