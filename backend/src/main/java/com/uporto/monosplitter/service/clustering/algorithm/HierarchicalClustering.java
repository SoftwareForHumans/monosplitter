package com.uporto.monosplitter.service.clustering.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.uporto.monosplitter.model.Edge;
import com.uporto.monosplitter.model.Node;

import smile.clustering.linkage.WardLinkage;

public class HierarchicalClustering implements IHierarchicalClustering {
	private HashMap<Integer, Integer> idCorrespondents = new HashMap<Integer, Integer>();

	public ArrayList<ArrayList<Node>> executeClustering(ArrayList<Node> graphNodes, ArrayList<Edge> graphEdges,
			int nrClusters, boolean removeNoDependenciesClasses) {

		double[][] data = convertListToMatrix(graphNodes, graphEdges, removeNoDependenciesClasses);

		smile.clustering.HierarchicalClustering modelWardLinkage = smile.clustering.HierarchicalClustering
				.fit(WardLinkage.of(data));

		int[] labelWardLinkage = modelWardLinkage.partition(nrClusters);
		Arrays.sort(labelWardLinkage);

		ArrayList<ArrayList<Node>> finalClusters = convertOutput(labelWardLinkage, removeNoDependenciesClasses,
				graphNodes);

		return finalClusters;
	}

	private Node getNodeCompleteNameById(int id, ArrayList<Node> graphNodes) {
		for (Node n : graphNodes) {
			if (n.getId() == id) {
				return n;
			}
		}
		return null;
	}

	private double[][] convertListToMatrix(ArrayList<Node> graphNodes, ArrayList<Edge> graphEdges,
			boolean removeNoDependenciesClasses) {
		double[][] data = null;

		if (removeNoDependenciesClasses) {
			int countNoDependenciesClasses = 0;

			for (Node n : graphNodes) {
				if (n.isHasDependency()) {
					idCorrespondents.put(countNoDependenciesClasses, n.getId());
					countNoDependenciesClasses++;
				}
			}
			data = new double[countNoDependenciesClasses][countNoDependenciesClasses];
		} else {
			data = new double[graphNodes.size()][graphNodes.size()];
		}

		for (int row = 0; row < data.length; row++) {
			for (int col = 0; col <= row; col++) {
				int[] ids = new int[2];

				if (removeNoDependenciesClasses) {
					ids[0] = idCorrespondents.get(row);
					ids[1] = idCorrespondents.get(col);
				} else {
					ids[0] = row + 1;
					ids[1] = col + 1;
				}

				double relationWeight = getRelationtWeight(ids[0], ids[1], graphEdges);

				data[row][col] = relationWeight;
				data[col][row] = relationWeight;
			}
		}

		return data;
	}

	private double getRelationtWeight(int firstNodeId, int secondNodeId, ArrayList<Edge> existentEdges) {
		double output = -1.0;
		int count = 0;

		for (Edge e : existentEdges) {
			if ((e.getSourceNode().getId() == firstNodeId && e.getDestinationNode().getId() == secondNodeId)
					|| (e.getSourceNode().getId() == secondNodeId && e.getDestinationNode().getId() == firstNodeId)) {
				output = e.getWeight();
				existentEdges.remove(count);
				break;
			}
			count++;
		}

		return output;
	}

	private ArrayList<ArrayList<Node>> convertOutput(int[] clusters, boolean removeNoDependenciesClasses,
			ArrayList<Node> graphNodes) {
		ArrayList<ArrayList<Node>> serviceCutsList = new ArrayList<ArrayList<Node>>();

		for (int i = 0; i < clusters.length; i++) {
			if (i > 0) {
				if (clusters[i] != clusters[i - 1]) {
					serviceCutsList.add(new ArrayList<Node>());
				}
				Node n;

				if (removeNoDependenciesClasses) {
					n = getNodeCompleteNameById(idCorrespondents.get(i), graphNodes);
				} else {
					n = getNodeCompleteNameById(i + 1, graphNodes);
				}
				serviceCutsList.get(serviceCutsList.size() - 1).add(n);

			} else {
				serviceCutsList.add(new ArrayList<Node>());
				Node n;
				if (removeNoDependenciesClasses) {
					n = getNodeCompleteNameById(idCorrespondents.get(i), graphNodes);
				} else {
					n = getNodeCompleteNameById(i + 1, graphNodes);
				}
				serviceCutsList.get(i).add(n);
			}
		}

		return serviceCutsList;
	}
}
