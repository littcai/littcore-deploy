package com.litt.core.deploy.core.protocol.cli;

import com.litt.core.util.StringUtils;

/**
 * Some useful assistant functions.
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
 * @since 2012-8-17
 * @version 1.0
 */
public class CliUtils {
	
	/**
	 * Checks if the command is support by target device.
	 * 
	 * @param response the response
	 * @return true, if is unsupport command
	 */
	public static boolean isUnsupportCommand(String response)
	{
		return StringUtils.endsWith(response, "command not found");
	}
	
	/**
	 * 移除内容中的ASNI格式的颜色字符.
	 * @param content
	 * @return
	 */
	public static String removeASNIColor(String content)
	{
		return content.replaceAll("\\x1b\\[[0-9]+(?:;[0-9]+){0,2}m", "");
	}
        	
	public static void main(String[] args)
		{
		String content = "aaa\nbbb\nccc";
		System.out.println("###########3");
		System.out.println(StringUtils.substringAfter(content, "\n"));
		System.out.println("###########3");
		System.out.println("###########3");
		System.out.println(StringUtils.substringBeforeLast(content, "\n"));
		System.out.println("###########3");
			}


}
