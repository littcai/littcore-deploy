package com.litt.core.deploy.core.protocol.cli.channel;

import com.litt.core.deploy.core.protocol.cli.CliListener;
import com.litt.core.exception.CheckedBusiException;

/**
 * CLI信道公共接口.
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
 * @since 2012-2-28
 * @version 1.0
 */
public interface ICliChannel {
	
	public static final String CHANNEL_TYPE_SSH = "SSH";
	public static final String CHANNEL_TYPE_TELNET = "TELNET";	
	
	public String execCommand(String command) throws CheckedBusiException;
	
	public String execCommand(String command, String pattern) throws CheckedBusiException;
	
	public void setPattern(String prompt, String pattern);
	
	public String getPrompt();
	
	public String getPattern();
	
	/**
	 * Update pattern.
	 *
	 * @throws CheckedBusiException the checked busi exception
	 */
	public void updatePattern() throws CheckedBusiException;
	
	public void setListener(CliListener listener);
	
	/**
	 * 创建新会话.
	 *
	 * @return the string
	 * @throws CheckedBusiException the checked busi exception
	 */
	public String createSession() throws CheckedBusiException;
	
	/**
	 * 关闭会话.
	 */
	public void closeSession();	
	
//	public boolean isConnected();
//	
//	/**
//	 * 关闭连接.
//	 *
//	 * @throws IOException Signals that an I/O exception has occurred.
//	 */
//	public void close()throws IOException;
//	
//	/**
//	 * 关闭连接（静默）.
//	 */
//	public void closeQuitely();
	
	
	

}
