package com.litt.core.deploy.core.protocol.cli.pool;

import java.io.IOException;

import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.deploy.core.protocol.cli.AccessInfo;
import com.litt.core.exception.BusiException;

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
public class TelnetCliConnection implements ICliConnection {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(TelnetCliConnection.class);	
	
	private TelnetClient client = new TelnetClient();
	
	private AccessInfo accessInfo;
	
	/** Telnet的认证与SSH的不同，需要人工管理. */
	private boolean isAuthenticated = false;
	
	/**
	 * Instantiates a new cli client.
	 *
	 * @param client the client
	 * @param accessInfo the access info
	 */
	public TelnetCliConnection(AccessInfo accessInfo) {
		super();
		this.accessInfo = accessInfo;
	}
	
	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public TelnetClient getConnection()
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
		//connect to server		
		try {
			TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler(  
                    "VT100", false, false, true, false);  
            EchoOptionHandler echoopt = new EchoOptionHandler(true, true,  
                    true, false);  
            SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true,  
                    true, true, true);  
  
            client.addOptionHandler(ttopt);  
            client.addOptionHandler(echoopt);
            client.addOptionHandler(gaopt);
			
			client.connect( accessInfo.getHost(), accessInfo.getPort() );	
			return client.isConnected();			
		} catch (IOException e) {
			throw new BusiException("Can't connect to host.", e);
		} catch (InvalidTelnetOptionException e) {
			throw new BusiException("Can't connect to host.", e);
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
		if(client.isConnected())
		{
			try {
				client.disconnect();
				isAuthenticated = false;
			} catch (IOException e) {
				logger.error("Telnet connection disconnect error.");
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.transoft.nms.core.protocol.cli.pool.ICliClient#isConnected()
	 */
	@Override
	public boolean isConnected()
	{
		return client.isConnected();
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

	/**
	 * @return the isAuthenticated
	 */
	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	/**
	 * @param isAuthenticated the isAuthenticated to set
	 */
	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

}
