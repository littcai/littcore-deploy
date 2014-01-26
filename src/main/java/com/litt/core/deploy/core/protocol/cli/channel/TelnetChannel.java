package com.litt.core.deploy.core.protocol.cli.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.deploy.core.protocol.cli.AccessInfo;
import com.litt.core.deploy.core.protocol.cli.pool.TelnetCliConnection;
import com.litt.core.exception.CheckedBusiException;

/**
 * .
 * 
 * <pre><b>Description：</b>
 *    
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-2-27
 * @version 1.0
 */
public class TelnetChannel extends BaseChannel implements ICliChannel {
	
	public static final Logger logger = LoggerFactory.getLogger(TelnetChannel.class);
	
	private TelnetCliConnection conn;
	
	/**
	 * @param host
	 * @param username
	 * @param password
	 */
	public TelnetChannel(TelnetCliConnection conn) {
		super();
		this.conn = conn;
	}

	public String createSession() throws CheckedBusiException
	{
		//connect to server		
		TelnetClient client = conn.getConnection();
		
		InputStream in = client.getInputStream();
		OutputStream out = client.getOutputStream();
		//init tool
		super.initTool(in, out);
		/*
		 * 用户登录，与SSH不同的是用户登录也基于流数据内容的判断
		 * 第一次判断是否已通过认证，通过的话直接使用
		 */
		if(!conn.isAuthenticated())
		{
			this.login();
			conn.setAuthenticated(true);
		}
		return "";	
	}
	
    public void closeSession() {
        
        try {
        	super.getTool().close();	//关闭异步读写的流处理器
            if(conn.isConnected())
            {
            	conn.disconnect();
                if(logger.isDebugEnabled())
                {
                	logger.debug("Session is closed.");
                }
            }
        } catch (IOException e) {
            logger.error("Close telnet session error.", e);
        }
    }
	
	/**
	 * 
	 */
	private void login() throws CheckedBusiException
	{
		String result = super.readUnit("ogin:");
//		System.out.println("...");
//		System.out.println(result);
		result = super.execCommand(conn.getAccessInfo().getUsername(), "assword:");
//		System.out.println("...");
//		System.out.println(result);
		result = super.execCommand(conn.getAccessInfo().getPassword());
//		System.out.println(result);
//		System.out.println("...");
		super.updatePattern();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception 
	{
		AccessInfo accessInfo = new AccessInfo("172.16.10.57", 23, "bob", "000000", "$");		
		
		TelnetChannel channel = ChannelFactory.createTelnetChannel(accessInfo);
		
		//TelnetChannel channel = new TelnetChannel("192.168.6.30", "rd", "transoft_rd");
		channel.setPattern("$", "$");
		long start = System.currentTimeMillis();
		channel.createSession();
		String result = channel.execCommand("ls");	
		result = channel.execCommand("ls");
		//System.out.println(result);
		System.out.println("#######################################");
		System.out.println(result.replaceAll("\033\\[[0-9]*m", "").replaceAll("\033\\[[0-9]*;[0-9]*m", ""));
		channel.closeSession();
		
//		
//		String result = channel.readUnit("ser:");
//		System.out.println("...");
//		System.out.println(result);
//		result = channel.sendCommand("rd", "assword:");
//		System.out.println("...");
//		System.out.println(result);
//		result = channel.execCommand("transoft_rd");
//		System.out.println(result);
//		System.out.println("...");
//		channel.updatePattern();
		
		
		//System.out.println("...");
//		System.out.println(channel.getPattern());
		//System.out.println("...");
//		System.out.println(channel.getPrompt());
//		//System.out.println("...");
//		result = channel.execCommand("help");
//		System.out.println(result);
//		System.out.println("...");
//		String result = channel.execCommand("ls");	
//		result = channel.execCommand("ls");
//		System.out.println("cost:"+(System.currentTimeMillis() - start));
//		System.out.println(result);
//		start = System.currentTimeMillis();
//		channel.sendCommand("ls");		
//		System.out.println("cost:"+(System.currentTimeMillis() - start));
//		System.out.println(result.replaceAll("\033\\[[0-9]*m", "").replaceAll("\033\\[[0-9]*;[0-9]*m", ""));
		
//		channel.close();
	}


}