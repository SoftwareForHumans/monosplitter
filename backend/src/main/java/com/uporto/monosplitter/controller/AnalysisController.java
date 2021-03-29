package com.uporto.monosplitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uporto.monosplitter.service.analysis.DynamicAnalysisService;
import com.uporto.monosplitter.service.analysis.StaticAnalysisService;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping(value = "/analysis")
public class AnalysisController {
	private final StaticAnalysisService staticAnalysisService;
	private final DynamicAnalysisService dynamicAnalysisService;

	@Autowired
	public AnalysisController(StaticAnalysisService staticAnalysisService, DynamicAnalysisService dynamicAnalysisService) {
		this.staticAnalysisService = staticAnalysisService;
		this.dynamicAnalysisService = dynamicAnalysisService;
	}
	
	@PostMapping(value = "/static")
	@ResponseBody
	public ResponseEntity<String> staticAnalysis() {
		try {
			staticAnalysisService.execute();
			return new ResponseEntity<String>(HttpStatus.OK);
		}catch(RuntimeException e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping(value = "/dynamic")
	public void dynamiAnalysis() {
		try {
			dynamicAnalysisService.execute();
		}catch(RuntimeException e) {
			
		}
	}
}
