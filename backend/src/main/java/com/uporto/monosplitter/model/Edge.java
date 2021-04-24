package com.uporto.monosplitter.model;

public class Edge {
	private Node sourceNode;
	private Node destinationNode;
	private double weight;
	
	public Edge(Node sourceNode, Node destinationNode, double weight) {
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.weight = weight;
	}

	public Node getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
	}

	public Node getDestinationNode() {
		return destinationNode;
	}

	public void setDestinationNode(Node destinationNode) {
		this.destinationNode = destinationNode;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return (obj instanceof Edge) && (toString().equals(obj.toString()));
	}

	@Override
	public String toString() {
		return "Edge [sourceNode=" + sourceNode + ", destinationNode=" + destinationNode + ", weight=" + weight + "]";
	}
}
