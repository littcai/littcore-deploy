package com.litt.core.deploy.core.protocol.cli.channel;

import com.litt.core.exception.CheckedBusiException;

/**
 * 信道回调函数.
 * 
 * <pre><b>Description：</b>
 *    通过封装信道的创建和销毁，使得业务代码中无需关心session的生命周期，直接使用
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2013-04-03
 * @version 1.0
 */
public interface IChannelCallback<T> {
	
	
	/**
	 * 回调函数.
	 * 方法执行前创建Session
	 * 方法执行完毕销毁Session
	 *
	 * @param channel the channel
	 * @throws CheckedBusiException the checked busi exception
	 */
	public T callback(ICliChannel channel) throws CheckedBusiException;

}
