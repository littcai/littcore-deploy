package com.litt.core.deploy.core.protocol.cli;

/**
 * 认证信息.
 * 
 * <pre><b>Description：</b>
 *    连接设备所需的信息
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-2-29
 * @version 1.0
 */
public class AccessInfo {
	
	/** The protocol. */
	private String protocol;
	
	private String host;
	
	private int port;
	
	private String username;
	
	private String password;	
	
	private String enPassword;
	
	private String prompt;
	
	private String pattern;
	
	private String enPrompt;
	
	private int retries = 3;
	
	private long timeout = 30000;

	/**
	 * 
	 */
	public AccessInfo() {
		super();
	}
	
	/**
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 */
	public AccessInfo(String host, int port, String username, String password, String prompt) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.prompt = prompt;
		this.pattern = prompt;
	}

	/**
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 */
	public AccessInfo(String host, int port, String username, String password, String prompt, String pattern) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.prompt = prompt;
		this.pattern = pattern;
	}
	
	public String toString()
	{
		return new StringBuilder().append(host).append(":").append(port).toString();
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the enPassword
	 */
	public String getEnPassword() {
		return enPassword;
	}

	/**
	 * @param enPassword the enPassword to set
	 */
	public void setEnPassword(String enPassword) {
		this.enPassword = enPassword;
	}

	/**
	 * @return the prompt
	 */
	public String getPrompt() {
		return prompt;
	}

	/**
	 * @param prompt the prompt to set
	 */
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	/**
	 * @return the enPrompt
	 */
	public String getEnPrompt() {
		return enPrompt;
	}

	/**
	 * @param enPrompt the enPrompt to set
	 */
	public void setEnPrompt(String enPrompt) {
		this.enPrompt = enPrompt;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((enPassword == null) ? 0 : enPassword.hashCode());
		result = prime * result
				+ ((enPrompt == null) ? 0 : enPrompt.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + port;
		result = prime * result + ((prompt == null) ? 0 : prompt.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccessInfo other = (AccessInfo) obj;
		if (enPassword == null) {
			if (other.enPassword != null)
				return false;
		} else if (!enPassword.equals(other.enPassword))
			return false;
		if (enPrompt == null) {
			if (other.enPrompt != null)
				return false;
		} else if (!enPrompt.equals(other.enPrompt))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (port != other.port)
			return false;
		if (prompt == null) {
			if (other.prompt != null)
				return false;
		} else if (!prompt.equals(other.prompt))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the retries
	 */
	public int getRetries() {
		return retries;
	}

	/**
	 * @param retries the retries to set
	 */
	public void setRetries(int retries) {
		this.retries = retries;
	}

	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
