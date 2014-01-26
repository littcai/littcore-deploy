package com.litt.core.deploy.module.action.plugin;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;

import org.apache.commons.io.FileUtils;
import org.mvel2.templates.TemplateRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;

import com.litt.core.deploy.core.model.Project;
import com.litt.core.deploy.gui.Gui;
import com.litt.core.deploy.module.action.ActionRequest;
import com.litt.core.deploy.module.action.IAction;
import com.litt.core.exception.BusiException;
import com.mchange.v2.c3p0.ComboPooledDataSource;


public class DBUpgradeAction implements IAction {
	
	private static final Logger logger = LoggerFactory.getLogger(ChmodAction.class);
	
	private String driver;
	private String url;
	private String username;
	private String password;

	@Override
	public void doAction(ActionRequest request) {
		request.log("### Start action:dbupgrade ###");
		//parse dynamic properties
		String driver = TemplateRuntime.eval(this.driver, request.getHostInfo().getProperties()).toString();
		String url = TemplateRuntime.eval(this.url, request.getHostInfo().getProperties()).toString();
		String username = TemplateRuntime.eval(this.username, request.getHostInfo().getProperties()).toString();
		String password = TemplateRuntime.eval(this.password, request.getHostInfo().getProperties()).toString();		
		
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(driver);
			dataSource.setJdbcUrl(url);
			dataSource.setUser(username);
			dataSource.setPassword(password);
		} catch (PropertyVetoException e) {
			request.error("Init datasource error.", e);
		}
		
		Project project = request.getProject();
		File dir = new File(Gui.HOME_PATH, project.getCode());
		File changelogFile = new File(dir, "dbupgrade");
		//将目标文件复制到classpath		
		try {
			File targetPath = new File(this.getClass().getResource("/dbupgrade").getFile());
			System.out.println(targetPath);
			FileUtils.copyDirectory(changelogFile, targetPath);
		} catch (IOException e) {
			request.error("Init datasource error.", e);
			throw new BusiException("Copy db upgrade scripts error.", e);
		}
		
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:dbupgrade/changelog.xml");
		liquibase.setContexts("production");
		liquibase.setResourceLoader(new DefaultResourceLoader());
		try {
			liquibase.afterPropertiesSet();
			request.error("Database is upgraded.");
		} catch (LiquibaseException e) {
			request.error("DB upgrade error.", e);
			throw new BusiException("DB upgrade error.", e);
		}
	}
	
	public static void main(String[] args)
	{
		DBUpgradeAction action = new DBUpgradeAction();
		action.setDriver("com.mysql.jdbc.Driver");
		action.setUrl("jdbc:mysql://172.16.10.57:3306/trannms_2.2?useOldAliasMetadataBehavior=true&useUnicode=true&characterEncoding=utf-8");
		action.setUsername("root");
		action.setPassword("000000");
		
		Project project = new Project("trannms-2.2");
		
		ActionRequest request = new ActionRequest();
		request.setProject(project);
		action.doAction(request);
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
