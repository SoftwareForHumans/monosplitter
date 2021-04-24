package com.uporto.monosplitter.model.dto.output;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.uporto.monosplitter.model.Edge;
import com.uporto.monosplitter.model.Node;

public class SystemModelOutputDto {
	
	private List<Node> nodes;
	private List<Edge> links;
	
	public SystemModelOutputDto() {
		nodes = new ArrayList<>();
		links = new ArrayList<>();
	}
	
	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = new ArrayList<Node>(nodes);
	}

	public List<Edge> getLinks() {
		return links;
	}

	public void setLinks(Graph<Node, DefaultWeightedEdge> graph) {
		this.links = new ArrayList<Edge>();
		Set<DefaultWeightedEdge> allEdges = graph.edgeSet();
		for(DefaultWeightedEdge e : allEdges) {
			Edge newEdge = new Edge(graph.getEdgeSource(e), graph.getEdgeTarget(e), graph.getEdgeWeight(e));
			this.links.add(newEdge);
		}
	}
}
