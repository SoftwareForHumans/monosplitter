package com.uporto.monosplitter.model;

import java.util.ArrayList;
import java.util.Date;

public class Commit {
	private String hash;
	private String author;
	private Date date;
	private String message;
	private ArrayList<Node> modifiedFiles;
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ArrayList<Node> getModifiedFiles() {
		return modifiedFiles;
	}
	public void setModifiedFiles(ArrayList<Node> modifiedFiles) {
		this.modifiedFiles = modifiedFiles;
	}
}
