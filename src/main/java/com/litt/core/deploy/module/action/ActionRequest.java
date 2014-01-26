
package com.litt.core.deploy.module.action;

import java.io.IOException;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;

import com.litt.core.deploy.core.model.HostInfo;
import com.litt.core.deploy.core.model.Project;
import com.litt.core.deploy.core.protocol.cli.AccessInfo;

/**
 * @author Administrator
 *
 */
public class ActionRequest {
	
	private Project project;
	
	private HostInfo hostInfo;
	
	private JTextPane console;
	
	public void log(final String log)
	{		
		appendLog(log);		
	}

	/**
	 * @param log
	 */
	private void appendLog(final String log) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {				
				final StyledDocument doc = console.getStyledDocument();		
				final HTMLDocument html = (HTMLDocument)console.getDocument();		
				
				try {
					html.insertBeforeEnd(html.getDefaultRootElement(), "<div>"+log+"</div>");
					
					console.setCaretPosition(console.getDocument().getLength());
					//console.setCaretPosition(html.getLength());					
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}		
		});
	}	
	
	public void error(String log)
	{
		appendLog(log);		
	}
	
	public void error(String log, Throwable throwable)
	{
		appendLog(log);
	}
	
	public AccessInfo getAccessInfo() {
		return hostInfo.getAccessInfo();
	}


	public JTextPane getConsole() {
		return console;
	}

	public void setConsole(JTextPane console) {
		this.console = console;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the hostInfo
	 */
	public HostInfo getHostInfo() {
		return hostInfo;
	}

	/**
	 * @param hostInfo the hostInfo to set
	 */
	public void setHostInfo(HostInfo hostInfo) {
		this.hostInfo = hostInfo;
	}
	
}
