package com.litt.core.deploy.core.protocol.cli;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.concurrent.Callable;

import com.litt.core.util.StringUtils;

/**
 * Response Handler.
 * 
 * <pre><b>Description：</b>
 *    Read response from another thread, 
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
public class CliResponseHandler implements Callable<String> {
	
	private InputStream in; 
	private PrintStream printer;
    private String pattern;    
    private String interactivePrompt;
    private long timeout;
    
    private long startTime;

	/**
	 * @param in
	 * @param pattern
	 */
	public CliResponseHandler(InputStream in, PrintStream printer, String pattern, long timeout) {
		super();
		this.in = in;
		this.printer = printer;
		this.pattern = pattern;
		this.timeout = timeout;
	}

	@Override
	public String call() throws Exception {
		startTime = System.currentTimeMillis();		
		char lastChar = pattern.charAt(pattern.length() - 1);
		String more = "(q)uit";
		char moreChar = more.charAt(more.length() - 1);
		
		StringBuilder sb = new StringBuilder();
		//byte[] buffer = new byte[1024];	
		char ch = (char) in.read();		
		while (true) {	//always loop, use another thread to deal with timeout
			sb.append(ch);
			//System.out.print(ch);
			if (ch == lastChar) {
				if (sb.toString().endsWith(pattern)) {
					//这里不去除最后的提示符，因为还有一个换行符存在，有外面处理
					return sb.toString();
				}
			}
//			else if (ch == moreChar) {
//				if (sb.toString().endsWith(more)) {
//					System.out.println("get more...");
//					printer.println("");
//					printer.flush();
//					System.out.println("flush");
//				}
//			}
			
			ch = (char) in.read();
			startTime = System.currentTimeMillis();	//reset start time
		}
	}

    /**
     * True timeout.
     *
     * @return true, if is timeout
     */
    public boolean isTimeout()
    {
    	if(startTime==0)
    		return false;
    	if((System.currentTimeMillis() - startTime)>=timeout)
    		return true;
    	else 
    		return false;
    }
    
    

}
