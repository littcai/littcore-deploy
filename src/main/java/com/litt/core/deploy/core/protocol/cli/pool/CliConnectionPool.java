package com.litt.core.deploy.core.protocol.cli.pool;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.deploy.core.protocol.cli.AccessInfo;
import com.litt.core.deploy.core.protocol.cli.channel.ICliChannel;
import com.litt.core.exception.CheckedBusiException;

/**
 * CLI连接池.
 * 
 * <pre><b>Description：</b>
 *    注：一个Connection连接状态下，修改远程用户名密码不影响连接的使用.
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-7-9
 * @version 1.0
 */
public class CliConnectionPool {

    private static Logger logger = LoggerFactory.getLogger(CliConnectionPool.class);
    
    private static final CliConnectionPool insatance = new CliConnectionPool();

    private ConcurrentHashMap<String, ICliConnection> connectCache = new ConcurrentHashMap<String, ICliConnection>();

    private CliConnectionPool() {
        // TBD
    }
    
    /**
     * 单例模式.
     *
     * @return single instance of CliConnectionPool
     */
    public static CliConnectionPool getInstance()
    {
    	return insatance;
    }

    /**
     * 获得一个连接.
     *
     * @param channelType the channel type
     * @param accessInfo the access info
     * @return ICliChannel
     * @throws CheckedBusiException the checked busi exception
     */
    public ICliConnection getConnection(String channelType, AccessInfo accessInfo) throws CheckedBusiException { 	
    	
    	if(connectCache.containsKey(accessInfo.getHost()))
    	{
    		ICliConnection conn = connectCache.get(accessInfo.getHost());
    		if(!conn.isConnected())	//如果连接池中的连接已断线，则重新连接
    			conn.reconnect();
    		return conn;
    	}
    	else
    	{
    		ICliConnection conn = createConnection(channelType, accessInfo);
    		if(ICliChannel.CHANNEL_TYPE_SSH.equals(channelType))	//telnet由于没有Session的概念，共享连接会导致共享上下文，不进行缓存，每次创建新的
    		{
    			connectCache.put(accessInfo.getHost(), conn);
    		}
    		return conn;
    	}	
    }
    
    /**
     * Creates the connection.
     *
     * @param channelType the channel type
     * @param accessInfo the access info
     * @return the i cli connection
     * @throws CheckedBusiException the checked busi exception
     */
    private ICliConnection createConnection(String channelType, AccessInfo accessInfo) throws CheckedBusiException
    {
    	if(ICliChannel.CHANNEL_TYPE_SSH.equals(channelType))
    	{
    		SSHCliConnection conn = new SSHCliConnection(accessInfo);
    		boolean isSuccess = conn.connect();
    		if(!isSuccess)
    			throw new CheckedBusiException("Connect Host:{} failed.", new String[]{accessInfo.getHost()});
    		return conn;
    	}
    	else if(ICliChannel.CHANNEL_TYPE_TELNET.equals(channelType))	
    	{
    		TelnetCliConnection conn = new TelnetCliConnection(accessInfo);
    		boolean isSuccess = conn.connect();
    		if(!isSuccess)
    			throw new CheckedBusiException("Connect Host:{} failed.", new String[]{accessInfo.getHost()});
    		return conn;
    	}
    	else
    	{
    		throw new CheckedBusiException("Unknown channelType:"+channelType);
    	}
    }    
    
    /**
     * 销毁连接池.
     */
    public void destory()
    {
    	Iterator<ICliConnection> iter = connectCache.values().iterator();
    	while(iter.hasNext())
    	{
    		ICliConnection conn = iter.next();
    		if(conn.isConnected())
    		{
				try {
					conn.disconnect();
				} catch (Exception e) {
					logger.error("Disconnect CLI error.", e);
				}
    		}	
    	}
    	//清除缓存
    	connectCache.clear();
    }

}
