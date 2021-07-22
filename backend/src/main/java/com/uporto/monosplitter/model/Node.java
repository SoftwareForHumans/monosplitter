package com.uporto.monosplitter.model;

public class Node {
	
	private int id;
	private String shortName;
	private String completeName;
	private boolean isInterface;
	private boolean hasDependency;
	
	public Node(int id, String shortName, String completeName) {
		this.id = id;
		this.shortName = shortName;
		this.completeName = completeName;
	}
	
	public Node(int id, String shortName, String completeName, boolean hasDependency) {
		this.id = id;
		this.shortName = shortName;
		this.completeName = completeName;
		this.hasDependency = hasDependency;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getCompleteName() {
		return completeName;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}
	
	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	public boolean isHasDependency() {
		return hasDependency;
	}

	public void setHasDependency(boolean hasDependency) {
		this.hasDependency = hasDependency;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return (obj instanceof Node) && (toString().equals(obj.toString()));
	}
	
	@Override
	public String toString() {
		return "Node [shortName=" + shortName + ", completeName=" + completeName + ", isInterface="
				+ isInterface + "]";
	}
}
