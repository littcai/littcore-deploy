package com.litt.core.deploy.core.protocol.cli;

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
 * @since 2012-3-5
 * @version 1.0
 */
public interface CliListener {
	
	public void onBeforeExecute(String command);
	
	public void onComplete(String command, String result);
	
}
