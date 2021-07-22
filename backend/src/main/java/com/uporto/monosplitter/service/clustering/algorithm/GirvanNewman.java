package com.uporto.monosplitter.service.clustering.algorithm;

import java.util.ArrayList;
import java.util.HashMap;

import org.gephi.clustering.api.Cluster;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import com.uporto.monosplitter.model.Edge;
import com.uporto.monosplitter.model.Node;

import cz.cvut.fit.krizeji1.girvan_newman.GirvanNewmanClusterer;

public class GirvanNewman implements IGirvanNewman {

	public ArrayList<ArrayList<Node>> executeClustering(ArrayList<Node> graphNodes, ArrayList<Edge> graphEdges,
			int nrClusters, boolean removeNoDependenciesClasses) {
		Lookup lookup = Lookup.getDefault();
		ProjectController pc = lookup.lookup(ProjectController.class);
		pc.newProject();
		@SuppressWarnings("unused")
		Workspace workspace = pc.getCurrentWorkspace();
		GraphController controller = Lookup.getDefault().lookup(GraphController.class);

		GraphModel model = controller.getModel();
		UndirectedGraph graph = model.getUndirectedGraph();

		setNodes(model, graph, graphNodes, removeNoDependenciesClasses);
		setEdges(model, graph, graphEdges);

		GirvanNewmanClusterer clusterer = new GirvanNewmanClusterer();
		clusterer.setPreferredNumberOfClusters(nrClusters);
		clusterer.execute(model);
		Cluster[] clusters = clusterer.getClusters();

		ArrayList<ArrayList<Node>> finalClusters = convertOutput(clusters, removeNoDependenciesClasses, graphNodes);

		return finalClusters;
	}

	private void setNodes(GraphModel model, UndirectedGraph graph, ArrayList<Node> graphNodes,
			boolean removeNoDependenciesClasses) {
		for (Node n : graphNodes) {
			if (removeNoDependenciesClasses) {
				if (n.isHasDependency()) {
					org.gephi.graph.api.Node gephiNode = model.factory().newNode();
					gephiNode.getNodeData().setLabel(n.getShortName());
					graph.setId(gephiNode, n.getCompleteName());
					graph.addNode(gephiNode);
				}
			} else {
				org.gephi.graph.api.Node gephiNode = model.factory().newNode();
				gephiNode.getNodeData().setLabel(n.getShortName());
				graph.setId(gephiNode, n.getCompleteName());
				graph.addNode(gephiNode);
			}
		}
	}

	private void setEdges(GraphModel model, UndirectedGraph graph, ArrayList<Edge> graphEdges) {
		for (Edge e : graphEdges) {
			org.gephi.graph.api.Edge gephiEdge = model.factory().newEdge(
					graph.getNode(e.getSourceNode().getCompleteName()),
					graph.getNode(e.getDestinationNode().getCompleteName()), (float) e.getWeight(), false);
			graph.addEdge(gephiEdge);
		}

	}

	private ArrayList<ArrayList<Node>> convertOutput(Cluster[] clusters, boolean removeNoDependenciesClasses,
			ArrayList<Node> graphNodes) {
		ArrayList<ArrayList<Node>> serviceCutsList = new ArrayList<ArrayList<Node>>();

		for (int i = 0; i < clusters.length; i++) {
			serviceCutsList.add(new ArrayList<Node>());
			for (int j = 0; j < clusters[i].getNodes().length; j++) {
				serviceCutsList.get(i)
						.add(getNodeByCompleteName(graphNodes, clusters[i].getNodes()[j].getNodeData().getId()));
			}
		}

		return serviceCutsList;
	}

	private Node getNodeByCompleteName(ArrayList<Node> graphNodes, String completeName) {
		for (Node n : graphNodes) {
			if (n.getCompleteName().equalsIgnoreCase(completeName)) {
				return n;
			}
		}
		return null;

	}
}
