
package com.litt.core.deploy.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class JobModel {
	
	private List<Step> stepList = new ArrayList<Step>();
	
	public Step getStep(int index)
	{
		return stepList.get(index);
	}
	
	public void add(Step step)
	{
		this.stepList.add(step);
	}

	public List<Step> getStepList() {
		return stepList;
	}

	public void setStepList(List<Step> stepList) {
		this.stepList = stepList;
	}
}
