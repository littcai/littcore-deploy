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


public class MkdirAction implements IAction {
	
	private static final Logger logger = LoggerFactory.getLogger(MkdirAction.class);
	
	private int mode;
	
	private List<String> fileList = new ArrayList<String>();
	
	public void add(String file)
	{
		fileList.add(file);
	}
	
	@Override
	public void doAction(ActionRequest request) {
		request.log("### Start action:mkdir ###");
		try {
			SftpChannel channel = ChannelFactory.createSftpChannel(request.getAccessInfo());
			channel.createSession();	     
			
			for(int i=0;i<fileList.size();i++)
			{
				String remoteFile = fileList.get(i);
				logger.debug("try to mkdir:{}", new Object[]{remoteFile});
				channel.mkdirs(remoteFile);
				request.log(StringUtils.format("mkdir:{}", new Object[]{remoteFile}));
			} 
			channel.closeSession();
		} catch (CheckedBusiException e) {
			throw new BusiException(e);
		} catch (IOException e) {
			throw new BusiException(e);
		}
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

	
}
