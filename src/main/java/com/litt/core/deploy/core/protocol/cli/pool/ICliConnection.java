package com.litt.core.deploy.core.protocol.cli.pool;

import com.litt.core.deploy.core.protocol.cli.AccessInfo;

public interface ICliConnection {

	/**
	 * 进行连接.
	 *
	 * @return true, if successful
	 */
	public abstract boolean connect();

	/**
	 * 重连.
	 */
	public abstract void reconnect();

	/**
	 * 断开连接.
	 */
	public abstract void disconnect();

	/**
	 * Checks if is connected.
	 *
	 * @return true, if is connected
	 */
	public abstract boolean isConnected();

	/**
	 * @return the accessInfo
	 */
	public abstract AccessInfo getAccessInfo();

	/**
	 * @param accessInfo the accessInfo to set
	 */
	public abstract void setAccessInfo(AccessInfo accessInfo);

}