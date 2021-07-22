package com.uporto.monosplitter.consts;

public final class ClusteringAlgorithmsConsts {
	public static final String GIRVAN_NEWMAN_ALGORITHM = "GN";
	public static final String K_SPANNING_TREE_ALGORITHM = "KST";
	public static final String LABEL_PROPAGATION_ALGORITHM = "LP";
	public static final String HIERARCHCAL_CLUSTERING_ALGORITHM = "HC";
	
	private ClusteringAlgorithmsConsts() {
		throw new AssertionError();
	}
}
