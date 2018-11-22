package com.rsm.sample.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rsm.sample.dao.SampleDao;
import com.rsm.sample.vo.DepChartVo;
import com.rsm.sample.vo.DepListVo;
import com.rsm.sample.vo.EmpListVo;
import com.rsm.sample.vo.EmpSaveVo;
import com.rsm.sample.vo.JobChartVo;
import com.rsm.sample.vo.JobListVo;

@Repository
public class SampleDaoImpl implements SampleDao {
	
	private static String namespace = "com.rsm.sample.sampleDao";
	private static int result = 0;
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<EmpListVo> getEmpList(String departmentId) {
		return sqlSession.selectList(namespace + ".selectEmpList", departmentId);
	}

	@Override
	public int setEmp(EmpSaveVo empSaveDto) throws Exception {
		result = sqlSession.update(namespace + ".updateEmp", empSaveDto);
		
		if (result < 1) {
			throw new Exception("Error: SampleDaoImpl.setEmp");
		}

		return result;
	}

	@Override
	public int delEmp(Long employeeId) throws Exception {
		result = sqlSession.delete(namespace + ".deleteEmp", employeeId);

		if (result > 0) {
			throw new Exception("Error: SampleDaoImpl.delEmp");
		}

		return result;
	}

	@Override
	public int insEmp(EmpSaveVo empInsVo) throws Exception {		
		result = sqlSession.insert(namespace + ".insertEmp", empInsVo);

		if (result < 1) {
			throw new Exception("Error: SampleDaoImpl.insEmp");
		}

		return result;
	}

	@Override
	public List<DepListVo> getDepList() {	
		return sqlSession.selectList(namespace + ".selectDepList");
	}

	@Override
	public List<JobListVo> getJobList() {		
		return sqlSession.selectList(namespace + ".selectJobList");
	}

	@Override
	public List<DepChartVo> getDepChart() {		
		return sqlSession.selectList(namespace + ".selectDepChart");
	}

	@Override
	public List<JobChartVo> getJobChart() {		
		return sqlSession.selectList(namespace + ".selectJobChart");
	}
}
