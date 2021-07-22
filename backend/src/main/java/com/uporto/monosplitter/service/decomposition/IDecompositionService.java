package com.uporto.monosplitter.service.decomposition;

import java.util.ArrayList;

import com.uporto.monosplitter.model.Node;
import com.uporto.monosplitter.model.SystemModel;

public interface IDecompositionService {
	SystemModel getSystemModel(boolean analyseRepository, String gitRepoCheckoutPath, String repoStartDate,
			String repoEndDate, boolean useSimalarity, boolean className, boolean packageName);

	ArrayList<ArrayList<Node>> getMonolithServiceCuts(SystemModel systemModel, String clusteringAlgorithm,
			int nrClusters, boolean removeNoDependenciesClasses);

}
