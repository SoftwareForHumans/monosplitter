package com.uporto.monosplitter.controller;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uporto.monosplitter.model.Node;
import com.uporto.monosplitter.model.dto.output.SystemModelOutputDto;
import com.uporto.monosplitter.service.decomposition.IDecompositionService;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/decomposition")
@RestController
public class DecompositionController {
	
	private IDecompositionService decompositionService;
	
	@Autowired
	public DecompositionController(IDecompositionService decompositionService) {
		this.decompositionService = decompositionService;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> getAll(){
		try {
			Graph<Node, DefaultWeightedEdge> systemGraph = decompositionService.getMonolith();
			decompositionService.getMonolithServiceCuts(systemGraph);
			return new ResponseEntity<String>(HttpStatus.OK);
		}catch(RuntimeException e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/model", produces = MediaType.APPLICATION_JSON_VALUE)
	public SystemModelOutputDto getSystemModel(){
		try {
			//get system model
			Graph<Node, DefaultWeightedEdge> systemGraph = decompositionService.getMonolith();
			
			//build outputdto
			SystemModelOutputDto output = new SystemModelOutputDto();
			output.setNodes(systemGraph.vertexSet());
			output.setLinks(systemGraph);
			
			return output;
		}catch(RuntimeException e) {
			throw new RuntimeException("Exception while building the model of the system", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/service-cuts")
	public ResponseEntity<String> getServiceCuts(){
		try {
			//decompositionService.getMonolithServiceCuts();
			return new ResponseEntity<String>(HttpStatus.OK);
		}catch(RuntimeException e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
}
