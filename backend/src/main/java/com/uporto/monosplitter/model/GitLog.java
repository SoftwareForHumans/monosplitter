package com.uporto.monosplitter.model;

import java.util.ArrayList;

public class GitLog {
	private ArrayList<Commit> commits;
	
	public GitLog(ArrayList<Commit> commits) {
		this.commits = commits;
	}

	public ArrayList<Commit> getCommits() {
		return commits;
	}

	public void setCommits(ArrayList<Commit> commits) {
		this.commits = commits;
	}
	
	
}
