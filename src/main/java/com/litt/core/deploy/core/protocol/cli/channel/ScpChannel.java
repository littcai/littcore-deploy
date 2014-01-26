package com.litt.core.deploy.core.protocol.cli.channel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.litt.core.deploy.core.protocol.cli.pool.SSHCliConnection;
import com.litt.core.exception.CheckedBusiException;
import com.sshtools.j2ssh.ScpClient;
import com.sshtools.j2ssh.connection.Channel;
import com.sshtools.j2ssh.connection.ChannelEventListener;

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
public class ScpChannel extends SSHChannel {
	
	private ScpClient scpClient; 
	
	public ScpChannel(SSHCliConnection conn)throws CheckedBusiException
	{
		super(conn);
		
		scpClient = new ScpClient(super.getConn().getConnection(), true, new ChannelEventListener(){

			@Override
			public void onChannelClose(Channel paramChannel) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChannelEOF(Channel paramChannel) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChannelOpen(Channel paramChannel) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDataReceived(Channel paramChannel, byte[] paramArrayOfByte) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDataSent(Channel paramChannel, byte[] paramArrayOfByte) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
	}
	
	public void upload(File localFile, String remotePath)
	{
		//scpClient.
	}
	
	/**
	 * 创建远程目录
	 * @param remotePath 远程目录
	 */
	public void mkdir(String remotePath)
	{
		
	}
	

	public void download(File localFile, String remoteFilePath) throws IOException
	{
		InputStream in = scpClient.get(remoteFilePath);
		OutputStream out = new FileOutputStream(localFile);
				
		int i = -1;
		while((i=in.read())!=-1)
		{
			out.write(i);
		}
		in.close();
		out.close();
	}
	
	public static void main(String[] args) throws Exception 
	{
//		ScpChannel channel = new ScpChannel("172.16.10.120", "bob", "000000");
//		channel.createSession();
//		channel.download(new File("c:\\snmpd.conf"), "snmpd.conf");
//		channel.close();
//		String result = channel.sendCommand("ls");
//		System.out.println(result);
	}
	
}
