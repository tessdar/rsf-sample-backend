package com.rsm.sample.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rsm.common.util.MessageReturn;
import com.rsm.common.util.MessageTrans;
import com.rsm.sample.service.SampleService;
import com.rsm.sample.vo.DepChartVo;
import com.rsm.sample.vo.DepListVo;
import com.rsm.sample.vo.EmpListVo;
import com.rsm.sample.vo.EmpSaveVo;
import com.rsm.sample.vo.JobChartVo;
import com.rsm.sample.vo.JobListVo;

@RestController
@RequestMapping("/emp")
public class SampleCtrl {

	private static Map<String, Object> result = new HashMap<String, Object>();
	private static String msg = null;

	@Autowired
	private SampleService service;

	@Autowired
	private MessageTrans messageTrans;

	@Autowired
	private MessageReturn messageReturn;

	@GetMapping(path = {"/list"})
	@ResponseBody
	public ResponseEntity<List<EmpListVo>> getEmpList(@RequestParam("departmentId") String departmentId) {

		List<EmpListVo> empLists = service.getEmpList(departmentId);

		return messageReturn.getRestRespList(empLists);
	}

	@PostMapping(path = "/save", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> setEmp(@RequestBody List<EmpSaveVo> vos) {

		result.clear();
		try {
			msg = service.setEmp(vos);
		} catch (Exception e) {
			msg = e.getMessage();
		}

		result = messageTrans.getMapLang(msg);

		return messageReturn.getRestResp(result, msg);
	}

	@PostMapping(path = "/del", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delEmp(@RequestBody List<EmpSaveVo> vos) {

		result.clear();
		try {
			msg = service.delEmp(vos);
		} catch (Exception e) {
			msg = e.getMessage();
		}

		result = messageTrans.getMapLang(msg);

		return messageReturn.getRestResp(result, msg);
	}

	@PutMapping(path = "/ins", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> insEmp(@RequestBody List<EmpSaveVo> vos) {

		result.clear();
		try {
			msg = service.insEmp(vos);
		} catch (Exception e) {
			msg = e.getMessage();
		}

		result = messageTrans.getMapLang(msg);

		return messageReturn.getRestResp(result, msg);
	}

	@GetMapping(path = "/dep")
	@ResponseBody
	public ResponseEntity<List<DepListVo>> getDepList() {

		List<DepListVo> depLists = service.getDepList();

		return messageReturn.getRestRespList(depLists);
	}

	@GetMapping(path = "/job")
	@ResponseBody
	public ResponseEntity<List<JobListVo>> getJobList() {

		List<JobListVo> jobLists = service.getJobList();

		return messageReturn.getRestRespList(jobLists);
	}

	@GetMapping(path = "/depChart")
	@ResponseBody
	public ResponseEntity<List<DepChartVo>> getDepChart() {

		List<DepChartVo> depCharts = service.getDepChart();

		return messageReturn.getRestRespList(depCharts);
	}

	@GetMapping(path = "/jobChart")
	@ResponseBody
	public ResponseEntity<List<JobChartVo>> getJobChart() {

		List<JobChartVo> jobCharts = service.getJobChart();

		return messageReturn.getRestRespList(jobCharts);
	}

}
