package com.litt.core.deploy.core.protocol.cli.channel;

import com.litt.core.deploy.core.protocol.cli.AccessInfo;
import com.litt.core.deploy.core.protocol.cli.pool.CliConnectionPool;
import com.litt.core.deploy.core.protocol.cli.pool.SSHCliConnection;
import com.litt.core.deploy.core.protocol.cli.pool.TelnetCliConnection;
import com.litt.core.exception.CheckedBusiException;

/**
 * 信道工厂类.
 * 
 * <pre><b>Description：</b>
 *    用于创建不同类型的信道
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-2-29
 * @version 1.0
 */
public abstract class ChannelFactory {
	
	/**
	 * 自适配获取信道对象.
	 *
	 * @param accessInfo the access info
	 * @return the i cli channel
	 * @throws CheckedBusiException the checked busi exception
	 */
	public static ICliChannel createChannel(AccessInfo accessInfo) throws CheckedBusiException
	{
		if(ICliChannel.CHANNEL_TYPE_SSH.equals(accessInfo.getProtocol()))
			return createSSHChannel(accessInfo);
		else if(ICliChannel.CHANNEL_TYPE_TELNET.equals(accessInfo.getProtocol()))
			return createTelnetChannel(accessInfo);
		else
			throw new CheckedBusiException("Unknown protocol:"+accessInfo.getProtocol());
	}
		
	/**
	 * 获取SSH的信道.
	 *
	 * @param channelType the channel type
	 * @return the channel
	 */
	public static SSHChannel createSSHChannel(AccessInfo accessInfo) throws CheckedBusiException
	{
		SSHCliConnection conn = (SSHCliConnection)CliConnectionPool.getInstance().getConnection(ICliChannel.CHANNEL_TYPE_SSH, accessInfo);			
		SSHChannel channel = new SSHChannel(conn);
		channel.setPattern(accessInfo.getPrompt(), accessInfo.getPattern());
		channel.setRetries(accessInfo.getRetries());
		channel.setTimeout(accessInfo.getTimeout());
		return channel;
	}
	
	/**
	 * 获取SFTP的信道.
	 *
	 * @param channelType the channel type
	 * @return the channel
	 */
	public static SftpChannel createSftpChannel(AccessInfo accessInfo) throws CheckedBusiException
	{
		SSHCliConnection conn = (SSHCliConnection)CliConnectionPool.getInstance().getConnection(ICliChannel.CHANNEL_TYPE_SSH, accessInfo);			
		SftpChannel channel = new SftpChannel(conn);
		channel.setPattern(accessInfo.getPrompt(), accessInfo.getPattern());
		channel.setRetries(accessInfo.getRetries());
		channel.setTimeout(accessInfo.getTimeout());
		return channel;
	}
	
	/**
	 * 获取Telnet的信道.
	 *
	 * @param channelType the channel type
	 * @return the channel
	 */
	public static TelnetChannel createTelnetChannel(AccessInfo accessInfo) throws CheckedBusiException
	{
		TelnetCliConnection conn = (TelnetCliConnection)CliConnectionPool.getInstance().getConnection(ICliChannel.CHANNEL_TYPE_TELNET, accessInfo);			
		TelnetChannel channel = new TelnetChannel(conn);
		channel.setPattern(accessInfo.getPrompt(), accessInfo.getPattern());
		channel.setRetries(accessInfo.getRetries());
		channel.setTimeout(accessInfo.getTimeout());
		return channel;
	}
	
	
}
