package com.litt.core.deploy.module.action.plugin;

import java.io.IOException;

import com.litt.core.deploy.core.protocol.cli.channel.ChannelFactory;
import com.litt.core.deploy.core.protocol.cli.channel.SSHChannel;
import com.litt.core.deploy.module.action.ActionRequest;
import com.litt.core.deploy.module.action.IAction;
import com.litt.core.exception.BusiException;
import com.litt.core.exception.CheckedBusiException;
import com.litt.core.util.StringUtils;


public class ShellAction implements IAction {
	
	private String name;
	
	private String script;
	
	public ShellAction(){}
	
	public ShellAction(String name, String script)
	{
		this.name = name;
		this.script = script;
	}

	@Override
	public void doAction(ActionRequest request) {
		request.log("### Start action:shell ###");
		try {
			SSHChannel channel = ChannelFactory.createSSHChannel(request.getAccessInfo());
			
			channel.createSession();
			String result = channel.execCommand(script);
			request.log(StringUtils.format("Exec:{}", new Object[]{script}));
			request.log(StringUtils.format("Result:{}", new Object[]{result}));
			channel.closeSession();
		}  catch (CheckedBusiException e) {
			throw new BusiException(e);
		} 
	}

	public String getName() {
		return name;
	}

	public String getScript() {
		return script;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
