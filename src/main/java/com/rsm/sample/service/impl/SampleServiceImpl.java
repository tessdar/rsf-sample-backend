package com.rsm.sample.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.rsm.common.util.MessageProp;
import com.rsm.common.vo.LoginVo;
import com.rsm.common.vo.UsersVo;
import com.rsm.sample.dao.SampleDao;
import com.rsm.sample.service.SampleService;
import com.rsm.sample.vo.DepChartVo;
import com.rsm.sample.vo.DepListVo;
import com.rsm.sample.vo.EmpListVo;
import com.rsm.sample.vo.EmpSaveVo;
import com.rsm.sample.vo.JobChartVo;
import com.rsm.sample.vo.JobListVo;

@Service
public class SampleServiceImpl implements SampleService {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(SampleServiceImpl.class);

	@Autowired
	private SampleDao dao;

	@Override
	public List<EmpListVo> getEmpList(String departmentId) {
		return dao.getEmpList(departmentId);
	}

	@Override
	public String setEmp(List<EmpSaveVo> vos) throws Exception {

		try {

			for (EmpSaveVo vo : vos) {

				if (vo.get_status() == 3) {
					dao.delEmp(vo.getEmployeeId());

				} else if (vo.get_status() == 1) {
					dao.insEmp(vo);

				} else if (vo.get_status() == 2) {
					dao.setEmp(vo);
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(MessageProp.ERR_SAVE.getMsg());
		}

		return MessageProp.INFO_SAVE.getMsg();
	}

	@Override
	public String delEmp(List<EmpSaveVo> vos) throws Exception {

		try {

			for (EmpSaveVo vo : vos) {
				dao.delEmp(vo.getEmployeeId());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(MessageProp.ERR_DEL.getMsg());
		}

		return MessageProp.INFO_DEL.getMsg();
	}

	@Override
	public String insEmp(List<EmpSaveVo> vos) throws Exception {

		try {

			for (EmpSaveVo vo : vos) {
				dao.insEmp(vo);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(MessageProp.ERR_INS.getMsg());
		}

		return MessageProp.INFO_INS.getMsg();
	}

	@Override
	@Cacheable(value = "DepList")
	public List<DepListVo> getDepList() {
		return dao.getDepList();
	}

	@Override
	@Cacheable(value = "JobList")
	public List<JobListVo> getJobList() {
		return dao.getJobList();
	}

	@Override
	public List<DepChartVo> getDepChart() {
		return dao.getDepChart();
	}

	@Override
	public List<JobChartVo> getJobChart() {
		return dao.getJobChart();
	}

	/**
	 * Cache Refresh 매일 새벽 3시에 실행
	 */
	@Override
	@CacheEvict(value = { "DepList", "JobList" })
	@Scheduled(cron = "0 0 3 * * ?")
	public void scheRefreshCache() {

	}

	@Override
	public UsersVo getUserOne(String userId) {
		return dao.getUserOne(userId);
	}

	@Override
	public String chkLogin(LoginVo loginVo) throws Exception {

		UsersVo usersVo = dao.getUserOne(loginVo.getUserId());

		if (usersVo == null) {
			throw new Exception(MessageProp.WRN_LOGIN_ID.getMsg());
		}

		if (!usersVo.getPassword().equals(loginVo.getPassword())) {
			throw new Exception(MessageProp.WRN_LOGIN_PW.getMsg());
		}

		return MessageProp.INFO_OK.getMsg();
	}

}
