package com.litt.core.deploy.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import net.miginfocom.swing.MigLayout;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.common.Utility;
import com.litt.core.deploy.core.model.HostInfo;
import com.litt.core.deploy.core.model.JobModel;
import com.litt.core.deploy.core.model.Project;
import com.litt.core.deploy.core.model.Step;
import com.litt.core.deploy.core.protocol.cli.AccessInfo;
import com.litt.core.deploy.module.action.ActionRequest;
import com.litt.core.deploy.module.action.IAction;
import com.litt.core.deploy.module.action.plugin.ChmodAction;
import com.litt.core.deploy.module.action.plugin.DBUpgradeAction;
import com.litt.core.deploy.module.action.plugin.DownloadAction;
import com.litt.core.deploy.module.action.plugin.MkdirAction;
import com.litt.core.deploy.module.action.plugin.RmAction;
import com.litt.core.deploy.module.action.plugin.ShellAction;
import com.litt.core.deploy.module.action.plugin.UploadAction;
import com.litt.core.exception.BusiException;
import com.litt.core.util.XmlUtils;

public class ConfigPanel extends JPanel {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigPanel.class);
	
	private JComboBox comboBoxProject;
	
	private Project currentProject;
	private JTextPane textPaneConsole;
	private JComboBox comboBoxTargetHost;
	private JScrollPane scrollPane;
	private JLabel lblStep;
	private JComboBox comboBoxStep;
	private JButton btnRun;
	private JLabel lblJob;
	private JComboBox comboBoxJob;
	
	public ConfigPanel() {
		setLayout(new MigLayout("", "[][grow]", "[][][][][][grow]"));
		
		JLabel lblProject = new JLabel("Project:");
		add(lblProject, "cell 0 0,alignx trailing");
		
		comboBoxProject = new JComboBox();
		add(comboBoxProject, "cell 1 0,growx");
		comboBoxProject.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				changeProject(e);				
			}			
		});
		
		JButton btnPublish = new JButton("Publish");
		btnPublish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				new SwingWorker<Void, Void>(){

					@Override
					protected Void doInBackground() throws Exception {						
						String jobName = getComboBoxJob().getSelectedItem().toString();
						
						JobModel job = readJob(currentProject, jobName);
						
						HostInfo hostInfo = (HostInfo)getComboBoxTargetHost().getSelectedItem();
						
						//set request
						ActionRequest request = new ActionRequest();
						request.setProject(currentProject);
						request.setHostInfo(hostInfo);
						request.setConsole(textPaneConsole);
						
						List<Step> stepList = job.getStepList();
						for(Step process : stepList)
						{
							List<IAction> actionList = process.getActionList();
							for(IAction action : actionList)
							{
								action.doAction(request);
							}
						}
						return null;
					}		
					
				}.execute();	
			}
		});
		
		lblJob = new JLabel("Job:");
		add(lblJob, "cell 0 1,alignx trailing");
		
		comboBoxJob = new JComboBox();
		comboBoxJob.setModel(new DefaultComboBoxModel(new String[] {"install", "upgrade", "uninstall"}));
		add(comboBoxJob, "cell 1 1,growx");
		comboBoxJob.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				changeJob(e);				
			}			
		});
		
		JLabel lblTargetHost = new JLabel("Target Host:");
		add(lblTargetHost, "cell 0 2,alignx trailing");
		
		comboBoxTargetHost = new JComboBox();
		add(comboBoxTargetHost, "cell 1 2,growx");
		add(btnPublish, "cell 1 3");
		
		lblStep = new JLabel("Step:");
		add(lblStep, "cell 0 4,alignx trailing");
		
		comboBoxStep = new JComboBox();
		add(comboBoxStep, "flowx,cell 1 4,growx");
		
		scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 5 2 2,grow");
		
		textPaneConsole = new JTextPane();
		
		scrollPane.setViewportView(textPaneConsole);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				new SwingWorker<Void, Void>(){

					@Override
					protected Void doInBackground() throws Exception {						
						execCurrentStep();	
						return null;
					}		
					
				}.execute();				
							
			}
			
		});
		add(btnRun, "cell 1 4");
		
		try {
			this.init();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	private void execCurrentStep() 
	{
		//run current step
		String jobName = getComboBoxJob().getSelectedItem().toString();
		
		JobModel job = readJob(currentProject, jobName);
		
		HostInfo hostInfo = (HostInfo)getComboBoxTargetHost().getSelectedItem();
		
		//set request
		ActionRequest request = new ActionRequest();
		request.setProject(currentProject);
		request.setHostInfo(hostInfo);
		request.setConsole(textPaneConsole);
		
		int index = getComboBoxStep().getSelectedIndex();
		Step step = job.getStep(index);
		List<IAction> actionList = step.getActionList();
		for(IAction action : actionList)
		{
			try {
				action.doAction(request);
			} catch (RuntimeException e) {
				e.printStackTrace();
				request.error("Run action error!", e);
			}
		}
		JOptionPane.showMessageDialog(this, "Success.");
	}
	
	public void init() throws Exception
	{
		//init project
		initProjectConf();
		//init host
		initHostInfo();
		//
		textPaneConsole.setContentType("text/html");
		final HTMLDocument html = (HTMLDocument)textPaneConsole.getDocument();
		html.setInnerHTML(html.getDefaultRootElement(), "<html></html>");
		this.configureHtmlEditorKit(textPaneConsole);
		textPaneConsole.addPropertyChangeListener("page", new PropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				scrollPane.getVerticalScrollBar().setValue(textPaneConsole.getHeight() - scrollPane.getVerticalScrollBar().getSize().height);   
				
			}
			
		});		
	}

	/**
	 * @throws Exception
	 */
	private void initHostInfo() throws Exception {
		File configFile =  new File(Gui.HOME_PATH, "hosts.xml");								
		
		Document document = XmlUtils.readXml(configFile);
		Element rootE = document.getRootElement();
		List hostList = rootE.elements();
		for(int i=0;i<hostList.size();i++)
		{
			Element hostE = (Element)hostList.get(i);		
			AccessInfo accessInfo = new AccessInfo();
			accessInfo.setHost(hostE.elementText("ip"));
			accessInfo.setPort(Utility.parseInt(hostE.elementText("port")));
			accessInfo.setUsername(hostE.elementText("username"));
			accessInfo.setPassword(hostE.elementText("password"));
			accessInfo.setPrompt(hostE.elementText("prompt"));
			accessInfo.setPattern(accessInfo.getPrompt());
			
			HostInfo hostInfo = new HostInfo(accessInfo);
			
			Element propertiesE = hostE.element("properties");
			List<Element> propertyList = propertiesE.elements();
			for(int j=0;j<propertyList.size();j++)
			{
				Element propertyE = propertyList.get(j);
				if(propertyE.isTextOnly())				
					hostInfo.putProperty(propertyE.getName(), propertyE.getText());	
				else	//递归
				{
					Map<String, Object> subMap = new HashMap<String, Object>();
					Iterator<Element> subElementList = propertyE.elementIterator();
					while(subElementList.hasNext())
					{
						Element subE = subElementList.next();
						subMap.put(subE.getName(), subE.getText());
					}
					hostInfo.putProperty(propertyE.getName(), subMap);
				}
			}
			this.getComboBoxTargetHost().addItem(hostInfo);			
		}
	}

	/**
	 * @throws Exception
	 */
	private void initProjectConf() throws Exception {
		File configFile =  new File(Gui.HOME_PATH+ File.separator+ "config.xml");								
		
		Document document = XmlUtils.readXml(configFile);
		Element rootE = document.getRootElement();
		List projectList = rootE.elements();
		for(int i=0;i<projectList.size();i++)
		{
			Element projectE = (Element)projectList.get(i);							
			String code = projectE.attributeValue("code");
			
			Project project = new Project(code);
			this.getComboBoxProject().addItem(project);
			if(i==0)
			{
				currentProject = project;
				initJobConf(project);
			}
		}
	}
	
	/**
	 * @throws Exception
	 */
	private void initJobConf(Project project) throws Exception {
		File dir =  new File(Gui.HOME_PATH, project.getCode());		
		
		File[] workflowList = dir.listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".xml"))
					return true;
				return false;
			}
		});
		
		this.getComboBoxJob().removeAllItems();
		if(workflowList!=null)
		{
			for(int i=0;i<workflowList.length;i++)
			{
				File workflowFile = workflowList[i];
				this.getComboBoxJob().addItem(workflowFile.getName());
			}
		}
	}
	
	private HTMLEditorKit configureHtmlEditorKit(JTextPane textPane) 
	{ 
		final HTMLEditorKit kit = (HTMLEditorKit) textPane.getEditorKit(); 
		final StyleSheet css = new StyleSheet(); 
		css.addRule("body { font-family: monospaced; margin-top: 0; margin-down: 0; line-height: 0; }"); 
		css.addRule("div, pre { margin-top: 0; margin-down: 0; line-height: 0; }"); 
		kit.setStyleSheet(css); 
		return kit; 
	}  
	
	private void changeProject(ItemEvent e) 
	{
		if(e.getStateChange()==ItemEvent.SELECTED)
		{
			try
			{
				Project project = (Project)e.getItem();														
				currentProject = project;	
				this.initJobConf(project);		
				if(this.getComboBoxJob().getItemCount()>0)
				{
					initStep(this.getComboBoxJob().getSelectedItem().toString());		
				}
			}
			catch (Exception e1)
			{
				logger.error("变更项目失败.", e1);
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}					
		}
	}
	
	private void changeJob(ItemEvent e) 
	{
		if(e.getStateChange()==ItemEvent.SELECTED)
		{
			try
			{
				String jobName = (String)e.getItem();
				initStep(jobName);
			}
			catch (Exception e1)
			{
				logger.error("变更项目失败.", e1);
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}					
		}
	}

	/**
	 * @param e
	 */
	private void initStep(String jobName) {
		//clear
		this.getComboBoxStep().removeAllItems();
		
		//读取流程信息，填充到STEP中
		JobModel job = this.readJob(currentProject, jobName);
		List<Step> stepList = job.getStepList();
		for(Step step : stepList)
		{
			this.getComboBoxStep().addItem(step);
		}
	}
	
	private JobModel readJob(Project project, String jobName) 
	{
		try {
			
			File configFile = new File(new File(Gui.HOME_PATH, project.getCode()), jobName);
			JobModel job = new JobModel();
			
			Document document = XmlUtils.readXml(configFile);
			Element rootE = document.getRootElement();
			List stepList = rootE.elements();
			for(int i=0;i<stepList.size();i++)
			{
				Element stepE = (Element)stepList.get(i);							
				String name = stepE.attributeValue("name");
				
				Step step = new Step();
				step.setName(name);
				
				//read action
				List<Element> actionEList = stepE.elements();
				for(int j=0;j<actionEList.size();j++)
				{
					Element actionE = (Element)actionEList.get(j);		
					String actionType = actionE.getName();
					//Upload
					if("upload".equals(actionType))
					{
						UploadAction action = new UploadAction();
						action.setType(actionE.attributeValue("type"));
						
						List<Element> fileList = actionE.elements();
						for(Element fileE : fileList)
						{
							String localPath = fileE.elementText("local-path");
							String remotePath = fileE.elementText("remote-path");
							
							action.addFile(localPath, remotePath);
						}
						step.add(action);
					}
					//Download
					if("download".equals(actionType))
					{
						DownloadAction action = new DownloadAction();
						action.setType(actionE.attributeValue("type"));
						
						List<Element> fileList = actionE.elements();
						for(Element fileE : fileList)
						{
							String localPath = fileE.elementText("local-path");
							String remotePath = fileE.elementText("remote-path");
							
							action.addFile(localPath, remotePath);
						}
						step.add(action);
					}
					//Chmod
					if("chmod".equals(actionType))
					{
						ChmodAction action = new ChmodAction();
						action.setMode(Utility.parseInt(actionE.attributeValue("mode")));
						List<Element> fileList = actionE.elements();
						for(Element fileE : fileList)
						{
							String filePath = fileE.getText();
							action.add(filePath);
						}
						step.add(action);
					}
					//rm
					if("rm".equals(actionType))
					{
						RmAction action = new RmAction();
						List<Element> fileList = actionE.elements();
						for(Element fileE : fileList)
						{
							String filePath = fileE.getText();
							action.add(filePath);
						}
						step.add(action);
					}
					//mkdir
					if("mkdir".equals(actionType))
					{
						MkdirAction action = new MkdirAction();
						List<Element> fileList = actionE.elements();
						for(Element fileE : fileList)
						{
							String filePath = fileE.getText();
							action.add(filePath);
						}
						step.add(action);
					}
					//Shell
					if("shell".equals(actionType))
					{
						ShellAction action = new ShellAction();
						action.setName(actionE.attributeValue("name"));
						action.setScript(actionE.elementText("script"));
						
						step.add(action);
					}
					//DB UPGRADE
					if("db-upgrade".equals(actionType))
					{
						DBUpgradeAction action = new DBUpgradeAction();									
						action.setDriver(actionE.elementText("jdbc-driver"));
						action.setUrl(actionE.elementText("jdbc-url"));
						action.setUsername(actionE.elementText("jdbc-username"));
						action.setPassword(actionE.elementText("jdbc-password"));
						
						step.add(action);
					}
				}
				
				job.add(step);
			}
			
			return job;
			
		} catch (FileNotFoundException e) {
			throw new BusiException("File not found.", e);
		}
		catch (Exception e) {
			throw new BusiException(e);
		}
	}

	public JComboBox getComboBoxProject() {
		return comboBoxProject;
	}
	public JTextPane getTextPaneConsole() {
		return textPaneConsole;
	}
	public JComboBox getComboBoxTargetHost() {
		return comboBoxTargetHost;
	}
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	public JComboBox getComboBoxStep() {
		return comboBoxStep;
	}
	public JComboBox getComboBoxJob() {
		return comboBoxJob;
	}
}
