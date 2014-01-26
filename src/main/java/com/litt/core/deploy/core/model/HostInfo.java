
package com.litt.core.deploy.core.model;

import java.util.HashMap;
import java.util.Map;

import com.litt.core.deploy.core.protocol.cli.AccessInfo;

/**
 * @author Administrator
 *
 */
public class HostInfo {
	
	private AccessInfo accessInfo;
	
	private Map<String, Object> properties = new HashMap<String, Object>();	
	
	public HostInfo(AccessInfo accessInfo) {
		super();
		this.accessInfo = accessInfo;
	}

	public void putProperty(String key, Object value)
	{
		properties.put(key, value);
	}
	
	public Object getProperty(String key)
	{
		if(!properties.containsKey(key))
			throw new IllegalArgumentException("Unkown property:"+key);
		return properties.get(key);
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(accessInfo);
		return builder.toString();
	}

	/**
	 * @return the accessInfo
	 */
	public AccessInfo getAccessInfo() {
		return accessInfo;
	}

	/**
	 * @param accessInfo the accessInfo to set
	 */
	public void setAccessInfo(AccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}

	/**
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
