package com.uporto.monosplitter.service.clustering.algorithm;

import java.util.ArrayList;

import com.uporto.monosplitter.model.Edge;
import com.uporto.monosplitter.model.Node;

public interface IGirvanNewman {
	
	ArrayList<ArrayList<Node>> executeClustering(ArrayList<Node> graphNodes, ArrayList<Edge> graphEdges, int nrClusters, boolean removeNoDependenciesClasses);
}
