
package com.litt.core.deploy.core.model;

import java.util.ArrayList;
import java.util.List;

import com.litt.core.deploy.module.action.IAction;

/**
 * @author Administrator
 *
 */
public class Step {
	
	private String name;
	
	private List<IAction> actionList = new ArrayList<IAction>();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Step [name=").append(name).append("]");
		return builder.toString();
	}
	
	public void add(IAction action)
	{
		actionList.add(action);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IAction> getActionList() {
		return actionList;
	}

	public void setActionList(List<IAction> actionList) {
		this.actionList = actionList;
	}

	

	
		
}
