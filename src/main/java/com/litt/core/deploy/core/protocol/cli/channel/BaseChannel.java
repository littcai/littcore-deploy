package com.litt.core.deploy.core.protocol.cli.channel;

import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.deploy.core.protocol.cli.CliHandler;
import com.litt.core.deploy.core.protocol.cli.CliListener;
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
 * @since 2012-2-28
 * @version 1.0
 */
public class BaseChannel {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(BaseChannel.class);	
	
	private long timeout = 30000;
	
	private int retries = 1;
	
	private CliHandler tool = new CliHandler();
	
	private CliListener listener;
	
	public void initTool(InputStream in, OutputStream out)
	{
		tool.setTimeout(timeout);
		tool.setIO(in, out);
	}
	
	public void setListener(CliListener listener)
	{
		this.listener = listener;
	}
	
	public void setPattern(String prompt, String pattern)
	{
		tool.setPrompt(prompt);
		tool.setPattern(pattern);
	}	
	
	public String getPattern()
	{
		return tool.getPattern();
	}
	
	public String getPrompt()
	{
		return tool.getPrompt();
	}
	
	/**
	 * Update pattern.
	 *
	 * @throws CheckedBusiException the checked busi exception
	 */
	public void updatePattern() throws CheckedBusiException
	{
		String pattern = this.fetchPattern();
		if(logger.isDebugEnabled())
		{
			logger.debug("Pattern:{}", new Object[]{pattern});
		}
		tool.setPattern(pattern);
	}
	
	/**
	 * Fetch pattern.
	 * use an empty command to get the whole prompt.
	 *
	 * @return the string
	 * @throws CheckedBusiException the checked busi exception
	 */
	public String fetchPattern() throws CheckedBusiException
	{
		return tool.fetchPattern();
	}
	
	public String execCommand(String command) throws CheckedBusiException
	{
		if(listener!=null)
			listener.onBeforeExecute(command);
		String result = tool.write(command);
		if(listener!=null)
			listener.onComplete(command, result);
		return result;
	}	
	
	public String execCommand(String command, String pattern) throws CheckedBusiException
	{
		if(listener!=null)
			listener.onBeforeExecute(command);
		String result = tool.write(command, pattern);
		if(listener!=null)
			listener.onComplete(command, result);
		return result;
	}
	
	public String readUnit() throws CheckedBusiException
	{
		return tool.readUnit();
	}
	
	public String readUnit(String pattern) throws CheckedBusiException
	{
		return tool.readUnit(pattern);
	}

	/**
	 * @return the tool
	 */
	public CliHandler getTool() {
		return tool;
	}

	/**
	 * @param tool the tool to set
	 */
	public void setTool(CliHandler tool) {
		this.tool = tool;
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

}
