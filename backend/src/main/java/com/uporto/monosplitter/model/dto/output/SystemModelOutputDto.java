package com.uporto.monosplitter.model.dto.output;

import java.util.ArrayList;

import com.uporto.monosplitter.model.Edge;
import com.uporto.monosplitter.model.Node;

public class SystemModelOutputDto {

	private ArrayList<Node> nodes;
	private ArrayList<Edge> links;

	public SystemModelOutputDto() {
		nodes = new ArrayList<>();
		links = new ArrayList<>();
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = new ArrayList<Node>(nodes);
	}

	public ArrayList<Edge> getLinks() {
		return links;
	}

	public void setLinks(ArrayList<Edge> edges) {
		this.links = new ArrayList<Edge>(edges);
	}
}
