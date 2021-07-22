package com.uporto.monosplitter.model.dto.output;

import java.util.ArrayList;

import org.gephi.clustering.api.Cluster;

import com.uporto.monosplitter.model.Node;

public class ServiceCutsOutputDto {
	
	private ArrayList<ArrayList<Node>> services;
	private int nrServices;
	
	public ServiceCutsOutputDto() {
		services = new ArrayList<ArrayList<Node>>();
		nrServices = 0;
	}

	public ArrayList<ArrayList<Node>> getServices() {
		return services;
	}

	public void setServices(ArrayList<ArrayList<Node>> clusters) {
		this.services = clusters;
	}
	
	public void setGNServices(Cluster[] clusters) {
		
	}

	public int getNrServices() {
		return nrServices;
	}

	public void setNrServices(int nrServices) {
		this.nrServices = nrServices;
	}
}
