package com.litt.core.deploy.core.protocol.cli.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.deploy.core.protocol.cli.AccessInfo;
import com.litt.core.deploy.core.protocol.cli.pool.SSHCliConnection;
import com.litt.core.exception.CheckedBusiException;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.session.SessionChannelClient;

/**
 * SSH方式命令下发.
 * 
 * <pre>
 * <b>Description：</b>
 * 	注：OpenSSH服务有最大10个session连接数的限制
 * </pre>
 * 
 * <pre>
 * <b>Changelog：</b>
 * 
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-2-27
 * @version 1.0
 */
public class SSHChannel extends BaseChannel implements ICliChannel {

    public static final Logger logger = LoggerFactory.getLogger(SSHChannel.class);    

    private SSHCliConnection conn;

    private SessionChannelClient session;    
    
    public SSHChannel(SSHCliConnection conn) throws CheckedBusiException
    {
       this.conn = conn;
    }

    /* (non-Javadoc)
     * @see com.transoft.nms.core.protocol.cli.channel.ICliChannel#createSession()
     */
    public String createSession() throws CheckedBusiException {
        // close old session
    	if(session!=null && !session.isClosed())
    	{
    		try {
				session.close();
			} catch (IOException e) {
				logger.error("Previous session closed error.", e);
			}
    	}
        try {
            // jsch
            // session = client.getSession(username, host, port);
            // session.setPassword(password);
            // //
            // java.util.Properties config = new java.util.Properties();
            // config.put("StrictHostKeyChecking", "no");
            // session.setConfig(config);
            // session.setTimeout(30000);
            //
            // PipedInputStream inputStream = new PipedInputStream();
            // PipedOutputStream out = new PipedOutputStream(inputStream);
            // session.setOutputStream(out);
            // //session.set.setExtOutputStream(out);
            //
            // PipedOutputStream outputStream = new PipedOutputStream();
            // session.setInputStream(new PipedInputStream(outputStream));
            //
            // //session.setSocketFactory(new JschSocketFactory(30000, 10000));
            // session.connect(30000); // making a connection with timeout.
            // System.out.println(session.isConnected());
            // Channel channel = session.openChannel("shell");
            // InputStream in = channel.getInputStream();
            // //OutputStream out = channel.getOutputStream();

//            client.connect(host, port, new IgnoreHostKeyVerification());
//            PasswordAuthenticationClient authentication = new PasswordAuthenticationClient();
//            authentication.setUsername(username);
//            authentication.setPassword(password);
//            client.authenticate(authentication);
        	SshClient client = conn.getConnection();
            session = client.openSessionChannel();
            // session.requestPseudoTerminal("vt100", 0, 25, 0, 0 , "");
            if (!session.requestPseudoTerminal("vt100", 0, 25, 0, 0, "")) {
                logger.warn("Failed to allocate a pseudo terminal");
            }
            String head = "";
            if (session.startShell()) {
                InputStream in = session.getInputStream();
                OutputStream out = session.getOutputStream();

                // init tool
                super.initTool(in, out);
                // read
                head = super.readUnit(); // read head
                
                // System.out.println("finish read head....");
                super.updatePattern(); // just init pattern
            } else
                logger.warn("Failed to start the users shell");

            return head;
            // } catch (JSchException e) {
            // throw new CheckedBusiException("Can't connect to host.", e);
        } catch (IOException e) {
            throw new CheckedBusiException("Can't connect to host.", e);
        }
    }

    
    @Override
    public void closeSession() {
        
        try {
        	super.getTool().close();	//关闭异步读写的流处理器
            if(session!=null && !session.isClosed())
            {
                session.close();
                if(logger.isDebugEnabled())
                {
                	logger.debug("Session is closed.");
                }
            }
        } catch (IOException e) {
            logger.error("Close ssh session error.", e);
        }
    }
    
    public <T> T call(IChannelCallback<T> callback) throws CheckedBusiException
    {
    	try {
			this.createSession();
			return callback.callback(this);
		} catch (CheckedBusiException e) {
			throw e;
		}
    	finally
    	{
    		this.closeSession();
    	}
    }


    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // SSHChannel channel = new SSHChannel("172.16.10.34", "bob", "000000");
        // channel.connect();
        // // System.out.println("...");
        // // System.out.println(channel.getPattern());
        // // System.out.println("...");
        // // System.out.println(channel.getPrompt());
        // // System.out.println("...");
        //
        // String result = channel.execCommand("ls");
        // result = channel.execCommand("ls");
        // System.out.println(result);
        //
        // channel.close();

        // HMC测试
        // SSHChannel channel = new SSHChannel("172.30.56.2", "hscroot",
        // "transoft");
        // channel.setPattern(":~>", ":~>");
        //
        // channel.connect();
        // System.out.println(channel.getPattern());
        // System.out.println(channel.getPrompt());
        // String result = channel.execCommand("lshmc -V");
        // System.out.println(result);
        // channel.close();
        
        
        AccessInfo accessInfo = new AccessInfo("172.16.10.57", 22, "root", "root", "#");
        
        //SSHCliConnection conn = new SSHCliConnection(accessInfo);
        SSHChannel channel = ChannelFactory.createSSHChannel(accessInfo);
        //SSHChannel channel = new SSHChannel(conn);
        //channel.setPattern(":~#", ":~#");
        channel.createSession();
        System.out.println(channel.getPattern());
        System.out.println(channel.getPrompt());
        channel.execCommand("export");
        String result = channel.execCommand("uname -a");
        System.out.println("result=" + result);
        channel.closeSession();
        
       //conn.disconnect();
    }

	/**
	 * @return the conn
	 */
	public SSHCliConnection getConn() {
		return conn;
	}

   

    
}