package com.uporto.monosplitter.model;

import java.util.ArrayList;

public class SystemModel {
	private ArrayList<Node> nodes;
	private ArrayList<Edge> links;
	private double maxStaticAnaysisRel;
	
	public SystemModel(ArrayList<Node> nodes, ArrayList<Edge> links) {
		this.nodes = nodes;
		this.links = links;
		maxStaticAnaysisRel = Double.NaN;
	}
	
	public SystemModel() {
		this.nodes = new ArrayList<Node>();
		this.links = new ArrayList<Edge>();
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<Edge> getLinks() {
		return links;
	}

	public void setLinks(ArrayList<Edge> links) {
		this.links = links;
	}
	
	public double getMaxStaticAnaysisRel() {
		return maxStaticAnaysisRel;
	}

	public void setMaxStaticAnaysisRel(double maxStaticAnaysisRel) {
		this.maxStaticAnaysisRel = maxStaticAnaysisRel;
	}
	
}
