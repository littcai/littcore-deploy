package com.litt.core.deploy.core.model;

public class Project {
	
	private String code;		

	public Project(String code) {
		super();
		this.code = code;
	}

	public String toString()
	{		
		return this.code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
