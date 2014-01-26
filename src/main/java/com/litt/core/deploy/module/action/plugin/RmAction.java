package com.litt.core.deploy.module.action.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.deploy.core.protocol.cli.channel.ChannelFactory;
import com.litt.core.deploy.core.protocol.cli.channel.SftpChannel;
import com.litt.core.deploy.module.action.ActionRequest;
import com.litt.core.deploy.module.action.IAction;
import com.litt.core.exception.BusiException;
import com.litt.core.exception.CheckedBusiException;
import com.litt.core.util.StringUtils;


public class RmAction implements IAction {
	
	private static final Logger logger = LoggerFactory.getLogger(RmAction.class);
	
	private List<String> fileList = new ArrayList<String>();
	
	public void add(String file)
	{
		fileList.add(file);
	}
	
	@Override
	public void doAction(ActionRequest request) {
		request.log("### Start action:rm ###");
		try {
			SftpChannel channel = ChannelFactory.createSftpChannel(request.getAccessInfo());
			channel.createSession();	     
			
			for(int i=0;i<fileList.size();i++)
			{
				String remoteFile = fileList.get(i);
				logger.debug("try to rm:{}", new Object[]{remoteFile});
				channel.rm(remoteFile);
				request.log(StringUtils.format("rm file:{}", new Object[]{remoteFile}));
			} 
			channel.closeSession();
		} catch (CheckedBusiException e) {
			throw new BusiException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusiException(e);
		}
	}

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

	
}
