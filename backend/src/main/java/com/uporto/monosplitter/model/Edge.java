package com.uporto.monosplitter.model;

public class Edge {
	private Node sourceNode;
	private Node destinationNode;
	private boolean staticAnalysisRelation;
	private int nrInterfaceCalls;
	private int nrMethodCalls;
	private int nrInstantiations;
	private int totalStaticAnalysisConnections;
	private boolean repositoryRelation;
	private double repositoryUpdateCorrelation;
	private double weight;

	public Edge(Node sourceNode, Node destinationNode) {
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.staticAnalysisRelation = false;
		this.repositoryRelation = false;
		this.nrInterfaceCalls = 0;
		this.totalStaticAnalysisConnections = 0;
		this.nrInstantiations = 0;
		this.nrMethodCalls = 0;
		this.repositoryUpdateCorrelation = Double.NaN;
		this.weight = Double.NaN;
	}

	public Edge(Node sourceNode, Node destinationNode, boolean staticAnalysisRelation, boolean repositoryRelation,
			int nrInterfaceCalls, int totalStaticAnalysisConnections, int nrInstantiations, int nrMethodCalls,
			double repositoryUpdateCorrelation, double weight) {
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.staticAnalysisRelation = staticAnalysisRelation;
		this.repositoryRelation = repositoryRelation;
		this.nrInterfaceCalls = nrInterfaceCalls;
		this.totalStaticAnalysisConnections = totalStaticAnalysisConnections;
		this.nrInstantiations = nrInstantiations;
		this.nrMethodCalls = nrMethodCalls;
		this.repositoryUpdateCorrelation = repositoryUpdateCorrelation;
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

	public boolean isStaticAnalysisRelation() {
		return staticAnalysisRelation;
	}

	public void setStaticAnalysisRelation(boolean staticAnalysisRelation) {
		this.staticAnalysisRelation = staticAnalysisRelation;
	}

	public int getNrInterfaceCalls() {
		return nrInterfaceCalls;
	}

	public void setNrInterfaceCalls(int nrInterfaceCalls) {
		this.nrInterfaceCalls = nrInterfaceCalls;
	}

	public int getNrMethodCalls() {
		return nrMethodCalls;
	}

	public void setNrMethodCalls(int nrMethodCalls) {
		this.nrMethodCalls = nrMethodCalls;
	}

	public int getNrInstantiations() {
		return nrInstantiations;
	}

	public void setNrInstantiations(int nrInstantiations) {
		this.nrInstantiations = nrInstantiations;
	}

	public int getTotalStaticAnalysisConnections() {
		return totalStaticAnalysisConnections;
	}

	public void setTotalStaticAnalysisConnections(int totalStaticAnalysisConnections) {
		this.totalStaticAnalysisConnections = totalStaticAnalysisConnections;
	}

	public boolean isRepositoryRelation() {
		return repositoryRelation;
	}

	public void setRepositoryRelation(boolean repositoryRelation) {
		this.repositoryRelation = repositoryRelation;
	}

	public double getRepositoryUpdateCorrelation() {
		return repositoryUpdateCorrelation;
	}

	public void setRepositoryUpdateCorrelation(double repositoryUpdateCorrelation) {
		this.repositoryUpdateCorrelation = repositoryUpdateCorrelation;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "Edge [sourceNode=" + sourceNode.getShortName() + ", destinationNode=" + destinationNode.getShortName()
				+ ", staticAnalysisRelation=" + staticAnalysisRelation + ", nrInterfaceCalls=" + nrInterfaceCalls
				+ ", nrMethodCalls=" + nrMethodCalls + ", nrInstanciations=" + nrInstantiations
				+ ", totalStaticAnalysisConnections=" + totalStaticAnalysisConnections + ", repositoryRelation="
				+ repositoryRelation + ", repositoryUpdateCorrelation=" + repositoryUpdateCorrelation + ", weight="
				+ weight + "]";
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
}
