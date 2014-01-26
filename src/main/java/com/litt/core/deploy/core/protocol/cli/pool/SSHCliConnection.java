package com.litt.core.deploy.core.protocol.cli.pool;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.deploy.core.protocol.cli.AccessInfo;
import com.litt.core.exception.BusiException;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;

/**
 * 命令行连接对象.
 * 
 * <pre><b>Description：</b>
 *    封装了Java具体实现的连接对象及连接参数
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2013-04-02
 * @version 1.0
 */
public class SSHCliConnection implements ICliConnection {
	
	private static final Logger logger = LoggerFactory.getLogger(SSHCliConnection.class);
	
	private SshClient client = new SshClient(); 
	
	private AccessInfo accessInfo;
	
	/**
	 * Instantiates a new cli client.
	 *
	 * @param client the client
	 * @param accessInfo the access info
	 */
	public SSHCliConnection(AccessInfo accessInfo) {
		super();
		this.accessInfo = accessInfo;
	}
	
	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public SshClient getConnection()
	{
		if(!this.isConnected())
			this.connect();
		return this.client;
	}
	
	/* (non-Javadoc)
	 * @see com.transoft.nms.core.protocol.cli.pool.ICliClient#connect()
	 */
	@Override
	public boolean connect()
	{
		try 
		{
			//client = new SshClient();   
			client.connect(accessInfo.getHost(), accessInfo.getPort(), new IgnoreHostKeyVerification());
			PasswordAuthenticationClient authentication = new PasswordAuthenticationClient();
			authentication.setUsername(accessInfo.getUsername());
			authentication.setPassword(accessInfo.getPassword());
			int result = client.authenticate(authentication);
			logger.info("SSH auth complete, result:{}", new Object[]{result});
			if (result == AuthenticationProtocolState.COMPLETE) {  
				
			}				
			return client.isConnected() && client.isAuthenticated();
		} catch (IOException e) {
			throw new BusiException("Initialize SSH client error.", e);
		}       
	}
	
	/* (non-Javadoc)
	 * @see com.transoft.nms.core.protocol.cli.pool.ICliClient#reconnect()
	 */
	@Override
	public void reconnect()
	{
		//先关闭旧的，再重连
		this.disconnect();
		//创新连接
		this.connect();
	}
	
	/* (non-Javadoc)
	 * @see com.transoft.nms.core.protocol.cli.pool.ICliClient#disconnect()
	 */
	@Override
	public void disconnect()
	{
		if(client!=null&&client.isConnected())
			client.disconnect();
	}
	
	/* (non-Javadoc)
	 * @see com.transoft.nms.core.protocol.cli.pool.ICliClient#isConnected()
	 */
	@Override
	public boolean isConnected()
	{
		return client!=null && client.isConnected() && client.isAuthenticated();
	}

	/* (non-Javadoc)
	 * @see com.transoft.nms.core.protocol.cli.pool.ICliClient#getAccessInfo()
	 */
	@Override
	public AccessInfo getAccessInfo() {
		return accessInfo;
	}

	/* (non-Javadoc)
	 * @see com.transoft.nms.core.protocol.cli.pool.ICliClient#setAccessInfo(com.transoft.nms.core.protocol.cli.AccessInfo)
	 */
	@Override
	public void setAccessInfo(AccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}

}
