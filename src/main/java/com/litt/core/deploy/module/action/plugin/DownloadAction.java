package com.litt.core.deploy.module.action.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.litt.core.deploy.core.protocol.cli.channel.ChannelFactory;
import com.litt.core.deploy.core.protocol.cli.channel.SftpChannel;
import com.litt.core.deploy.module.action.ActionRequest;
import com.litt.core.deploy.module.action.IAction;
import com.litt.core.exception.BusiException;
import com.litt.core.exception.CheckedBusiException;
import com.litt.core.util.StringUtils;


/**
 * @author Administrator
 *
 */
public class DownloadAction implements IAction {
	
	private String type;
	
	private List<String> localFileList = new ArrayList<String>();
	
	private List<String> remoteFileList = new ArrayList<String>();
	
	public void addFile(String localFile, String remoteFile)
	{
		localFileList.add(localFile);
		remoteFileList.add(remoteFile);
	}	

	/* (non-Javadoc)
	 * @see com.litt.core.deploy.core.model.IAction#doAction()
	 */
	@Override
	public void doAction(ActionRequest request) {
		request.log("### Start action:download ###");
		//SFTP download
		try {
			SftpChannel channel = ChannelFactory.createSftpChannel(request.getAccessInfo());
			channel.createSession();	     
			
			for(int i=0;i<localFileList.size();i++)
			{
				String localFile = localFileList.get(i);
				String remoteFile = remoteFileList.get(i);
				
				channel.download(localFile, remoteFile);
				request.log(StringUtils.format("Remote file:{} download into {}", new Object[]{remoteFile, localFile}));
			} 
			channel.closeSession();
		} catch (CheckedBusiException e) {
			throw new BusiException(e);
		} catch (IOException e) {
			throw new BusiException(e);
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getLocalFileList() {
		return localFileList;
	}

	public List<String> getRemoteFileList() {
		return remoteFileList;
	}

}
