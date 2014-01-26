package com.litt.core.deploy.core.protocol.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.common.Utility;
import com.litt.core.exception.CheckedBusiException;
import com.litt.core.util.StringUtils;

/**
 * CLI交互处理.
 * 
 * <pre><b>Description：</b>
 *    IO交互处理，基于模式匹配+Timeout。
 *    注：目前的Timeout是基于经验值预估的，不好控制，如果某个命令执行时间很长的话，很有可能出现超时但执行正确的情况
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
public class CliHandler {
	
	public static final Logger logger = LoggerFactory.getLogger(CliHandler.class);
	
	ExecutorService service = Executors.newFixedThreadPool(1);	//use single-thread
	
	public static final long DEFAULT_TIMEOUT = 30000;
	
	private InputStream in;  
    private OutputStream out;
    private PrintStream printer;
    
    private String prompt = "$";
    private String pattern = "$";  
    private String lineBreak = "\n";
    private long timeout = DEFAULT_TIMEOUT;
    
    public CliHandler()
    {
    	
    }
    
    public CliHandler(InputStream in, OutputStream out)
    {
    	this.in = in;
    	this.out = out;
    	printer = new PrintStream(out);
    }  
    
    public void setIO(InputStream in, OutputStream out)
    {
    	this.in = in;
    	this.out = out;
    	printer = new PrintStream(out);
    }  
    
    public String fetchPattern() throws CheckedBusiException
    {
    	printer.print("");
    	printer.print(lineBreak);	//Linux only needs \n to create new line, if use println, "\r" will send to host and create another new line
    	printer.flush();
    	String ret = readUnit(pattern);
		if (logger.isDebugEnabled()) {
			logger.debug("write command, current pattern is:{}, String ret={}", new Object[]{pattern, ret}); //$NON-NLS-1$
		}    	    	
    	return StringUtils.substringAfterLast(ret, lineBreak);
    }
    
    public String write(String command) throws CheckedBusiException
    {      	
    	return write(command, pattern);
    }
    
    public String write(String command, String pattern) throws CheckedBusiException
    {      	
    	printer.print(command);
    	printer.print(lineBreak);	//Linux only needs \n to create new line, if use println, "\r" will send to host and create another new line
    	printer.flush();
    	String ret = readUnit(pattern);
		if (logger.isDebugEnabled()) {
			logger.debug("write command:{} , String ret={}", new Object[]{command, ret}); //$NON-NLS-1$
		}    	
    	//需要去掉第一行命令
    	ret = StringUtils.substringAfter(ret, lineBreak);
    	//如果命令无结果返回，剩余内容将不包含换行符，只剩一个提示符，需返回空
    	if(pattern.equals(ret))
    		return "";
    	//检查是否还有一行，有的话去掉
    	//ret = StringUtils.substringBeforeLast(ret, "\r");
    	ret = StringUtils.substringBeforeLast(ret, lineBreak);
    	if(StringUtils.endsWith(ret, "\r"))
    		ret = ret.substring(0, ret.length()-1);
    	if (logger.isDebugEnabled()) {
			logger.debug("Final ret=" + ret); //$NON-NLS-1$
		}  
    	
    	return ret;
    }
    
    public String readUnit() throws CheckedBusiException
    {
    	return this.readUnit(pattern);
    }
    
    public String readUnit(String pattern) throws CheckedBusiException
    {
    	CliResponseHandler task = new CliResponseHandler(in, printer, pattern, timeout);
    	Future<String> future = service.submit(task);    	
    	try {
			return future.get(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new CheckedBusiException(e);
		} catch (ExecutionException e) {
			throw new CheckedBusiException(e);
		} catch (TimeoutException e) {
			logger.error("read pattern:{} timeout.", new Object[]{pattern});
			throw new CheckedBusiException(e);
		}
    }
    
    public void close() throws IOException
    {
    	out.close();
    	in.close();
    	service.shutdownNow();
    }
    
    public void closeQuitely()
    {
    	try {
			this.close();
		} catch (IOException e) {
			logger.error("Close cli io error.", e);
		}
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
     * Gets the pattern.
     *
     * @return the pattern
     */
    public String getPattern()
    {
    	return pattern;
    }

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the lineBreak
	 */
	public String getLineBreak() {
		return lineBreak;
	}

	/**
	 * @param lineBreak the lineBreak to set
	 */
	public void setLineBreak(String lineBreak) {
		this.lineBreak = lineBreak;
	}

}
