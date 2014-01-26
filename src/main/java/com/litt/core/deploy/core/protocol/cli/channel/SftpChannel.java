package com.litt.core.deploy.core.protocol.cli.channel;

import java.io.File;
import java.io.IOException;

import com.litt.core.deploy.core.protocol.cli.AccessInfo;
import com.litt.core.deploy.core.protocol.cli.pool.SSHCliConnection;
import com.litt.core.exception.CheckedBusiException;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;

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
public class SftpChannel extends SSHChannel {
	
	private SftpClient sftpClient;

	public SftpChannel(SSHCliConnection conn)throws CheckedBusiException
	{
		super(conn);
	}

	/**
	 * @throws CheckedBusiException
	 */
	public String createSession() throws CheckedBusiException {
		try {
			
			SshClient client = super.getConn().getConnection();	
			sftpClient = client.openSftpClient();
			return "";
		} catch (IOException e) {
			throw new CheckedBusiException("Init sftp channel error.", e);
		}
	}	
	
	/**
	 * 上传文件.
	 * @param localFile 本地文件
	 * @param remotePath 远程路径
	 * @throws IOException
	 */
	public void upload(String localFile, String remotePath) throws IOException
	{
		File file = new File(localFile);
		if(!file.exists())
			throw new IllegalArgumentException("Local file:"+localFile+" is not exist.");
		sftpClient.put(localFile, remotePath);
	}
	
	public void download(String localFile, String remoteFile) throws IOException
	{
		File file = new File(localFile);
		File dir = file.getParentFile();
		if(!dir.exists())
			dir.mkdirs();
		sftpClient.lcd(dir.getAbsolutePath());
		sftpClient.get(remoteFile, file.getAbsolutePath());
	}
	
	
	/**
	 * 创建远程目录
	 * @param remotePath 远程目录
	 */
	public void mkdir(String remotePath) throws IOException
	{
		sftpClient.mkdir(remotePath);
	}
	
	/**
	 * 创建远程目录
	 * @param remotePath 远程目录
	 */
	public void mkdirs(String remotePath) throws IOException
	{
		sftpClient.mkdirs(remotePath);
	}
	
	/**
	 * 删除远程文件或目录.
	 * @param remoteFile 远程文件或目录
	 * @throws IOException
	 */
	public void rm(String remoteFile)throws IOException
	{
		sftpClient.rm(remoteFile, true, true);
	}
	
	/**
	 * 重命名.
	 * @param srcFile 原文件
	 * @param newFile 新文件
	 * @throws IOException
	 */
	public void rename(String srcFile, String newFile)throws IOException
	{
		sftpClient.rename(srcFile, newFile);
	}
	
	/**
	 * 修改文件权限.
	 * 0表示没有权限，1表示可执行权限， 2表示可写权限，4表示可读权限
	 * 数字属性的格式应为3个从0到7的 八进制数，其顺序是（u）（g）（o）
	 * 
	 * 0400       Individual read
     * 0200       Individual write
     * 0100       Individual execute (or list directory)
     * 0040       Group read
     * 0020       Group write
     * 0010       Group execute
     * 0004       Other read
     * 0002       Other write
     * 0001       Other execute
	 * 
	 * @param mode
	 * @param remoteFile
	 */
	public void chmod(int mode, String remoteFile)throws IOException
	{	
		sftpClient.chmod(mode, remoteFile);
	}
	
	public void chgrp(int mode, String remoteFile)throws IOException
	{	
		sftpClient.chgrp(mode, remoteFile);
	}
	
	public void chown(int mode, String remoteFile)throws IOException
	{	
		sftpClient.chown(mode, remoteFile);
	}
	
	public void closeSession()
	{
		if(sftpClient!=null && !sftpClient.isClosed())
		{
			try {
				sftpClient.quit();
			} catch (IOException e) {
				logger.error("Close ssh sftp client error.", e);
			}
		}
		super.getConn().disconnect();	
	}
	
	public static void main(String[] args) throws Exception {
		
		 AccessInfo accessInfo = new AccessInfo("172.16.10.57", 22, "root", "root", "#");
		 SftpChannel channel = ChannelFactory.createSftpChannel(accessInfo);
	     channel.createSession();
	     
	     //channel.mkdir("abc");
	     //channel.upload("c:\\lonfile.txt", "/usr");
	     //channel.download("c:\\log.log", "log.log");
	     channel.chmod(777, "/usr/local/demo/demo.sh");
	     //channel.closeSession();
	}
}
