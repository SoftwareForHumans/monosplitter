package com.uporto.monosplitter.service.decomposition;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.uporto.monosplitter.model.Node;

public interface IDecompositionService {
	Graph<Node, DefaultWeightedEdge> getMonolith();
	void getMonolithServiceCuts(Graph<Node, DefaultWeightedEdge> monolith);
}
