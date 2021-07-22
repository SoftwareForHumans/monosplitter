package com.uporto.monosplitter.model.dto.input;

import com.uporto.monosplitter.model.SystemModel;

public class DecompositionSpecsInputDto {
	
	private SystemModel systemModel;
	private String clusteringAlgorithm;
	private int nrClusters;
	private boolean executeRepoAnalysis;
	private String repoCheckoutPath;
	private String repoStartDate;
	private String repoEndDate;
	private boolean useSimilarity;
	private boolean className;
	private boolean packageName;
	private boolean removeNoDependenciesClasses;
	
	public DecompositionSpecsInputDto() {
	}

	public String getClusteringAlgorithm() {
		return clusteringAlgorithm;
	}

	public void setClusteringAlgorithm(String clusteringAlgorithm) {
		this.clusteringAlgorithm = clusteringAlgorithm;
	}

	public int getNrClusters() {
		return nrClusters;
	}

	public void setNrClusters(int nrClusters) {
		this.nrClusters = nrClusters;
	}

	public SystemModel getSystemModel() {
		return systemModel;
	}

	public void setSystemModel(SystemModel systemModel) {
		this.systemModel = systemModel;
	}

	public boolean isExecuteRepoAnalysis() {
		return executeRepoAnalysis;
	}

	public void setExecuteRepoAnalysis(boolean executeRepoAnalysis) {
		this.executeRepoAnalysis = executeRepoAnalysis;
	}

	public String getRepoCheckoutPath() {
		return repoCheckoutPath;
	}

	public void setRepoCheckoutPath(String repoCheckoutPath) {
		this.repoCheckoutPath = repoCheckoutPath;
	}

	public String getRepoStartDate() {
		return repoStartDate;
	}

	public void setRepoStartDate(String repoStartDate) {
		this.repoStartDate = repoStartDate;
	}

	public String getRepoEndDate() {
		return repoEndDate;
	}

	public void setRepoEndDate(String repoEndDate) {
		this.repoEndDate = repoEndDate;
	}

	public boolean isUseSimilarity() {
		return useSimilarity;
	}

	public void setUseSimilarity(boolean useSimilarity) {
		this.useSimilarity = useSimilarity;
	}

	public boolean isClassName() {
		return className;
	}

	public void setClassName(boolean className) {
		this.className = className;
	}

	public boolean isPackageName() {
		return packageName;
	}

	public void setPackageName(boolean packageName) {
		this.packageName = packageName;
	}

	public boolean isRemoveNoDependenciesClasses() {
		return removeNoDependenciesClasses;
	}

	public void setRemoveNoDependenciesClasses(boolean removeNoDependenciesClasses) {
		this.removeNoDependenciesClasses = removeNoDependenciesClasses;
	}
}
