package com.uporto.monosplitter.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uporto.monosplitter.model.Node;
import com.uporto.monosplitter.model.SystemModel;
import com.uporto.monosplitter.model.dto.input.DecompositionSpecsInputDto;
import com.uporto.monosplitter.model.dto.output.AllOutputDto;
import com.uporto.monosplitter.model.dto.output.ServiceCutsOutputDto;
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

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AllOutputDto getAll(@RequestBody DecompositionSpecsInputDto decompositionSpecsInputDto) {
		try {
			SystemModelOutputDto systemModel = new SystemModelOutputDto();
			ServiceCutsOutputDto serviceCuts = new ServiceCutsOutputDto();
			AllOutputDto output = new AllOutputDto();

			// get system model
			SystemModel systemGraph = decompositionService.getSystemModel(
					decompositionSpecsInputDto.isExecuteRepoAnalysis(),
					decompositionSpecsInputDto.getRepoCheckoutPath(), decompositionSpecsInputDto.getRepoStartDate(),
					decompositionSpecsInputDto.getRepoEndDate(), decompositionSpecsInputDto.isUseSimilarity(),
					decompositionSpecsInputDto.isClassName(), decompositionSpecsInputDto.isPackageName());

			// build output dto
			systemModel.setNodes(systemGraph.getNodes());
			systemModel.setLinks(systemGraph.getLinks());
			
			// get service cuts
			ArrayList<ArrayList<Node>> clusters = decompositionService.getMonolithServiceCuts(systemGraph,
					decompositionSpecsInputDto.getClusteringAlgorithm(), decompositionSpecsInputDto.getNrClusters(),
					decompositionSpecsInputDto.isRemoveNoDependenciesClasses());

			serviceCuts.setNrServices(clusters.size());
			serviceCuts.setServices(clusters);

			output.setSystemModel(systemModel);
			output.setServiceCuts(serviceCuts);

			return output;
		} catch (RuntimeException e) {
			throw new RuntimeException("Something went wrong", e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/model", produces = MediaType.APPLICATION_JSON_VALUE)
	public SystemModelOutputDto getSystemModel(@RequestBody DecompositionSpecsInputDto decompositionSpecsInputDto) {
		try {
			// get system model
			SystemModel systemGraph = decompositionService.getSystemModel(
					decompositionSpecsInputDto.isExecuteRepoAnalysis(),
					decompositionSpecsInputDto.getRepoCheckoutPath(), decompositionSpecsInputDto.getRepoStartDate(),
					decompositionSpecsInputDto.getRepoEndDate(), decompositionSpecsInputDto.isUseSimilarity(),
					decompositionSpecsInputDto.isClassName(), decompositionSpecsInputDto.isPackageName());

			// build output dto
			SystemModelOutputDto output = new SystemModelOutputDto();
			output.setNodes(systemGraph.getNodes());
			output.setLinks(systemGraph.getLinks());

			return output;
		} catch (RuntimeException e) {
			throw new RuntimeException("Exception while building the model of the system", e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/service-cuts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceCutsOutputDto getServiceCuts(@RequestBody DecompositionSpecsInputDto decompositionSpecsInputDto) {
		try {
			SystemModel systemModel = decompositionSpecsInputDto.getSystemModel();

			// get set of clusters
			ArrayList<ArrayList<Node>> clusters = decompositionService.getMonolithServiceCuts(systemModel,
					decompositionSpecsInputDto.getClusteringAlgorithm(), decompositionSpecsInputDto.getNrClusters(),
					decompositionSpecsInputDto.isRemoveNoDependenciesClasses());

			// build output dto
			ServiceCutsOutputDto output = new ServiceCutsOutputDto();
			output.setNrServices(clusters.size());
			output.setServices(clusters);

			return output;
		} catch (RuntimeException e) {
			throw new RuntimeException("Exception while detecting service cuts", e);
		}
	}
}
