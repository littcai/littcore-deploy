package com.litt.core.deploy.gui;

import java.util.HashMap;
import java.util.Map;

import org.mvel.MVEL;
import org.mvel.MacroProcessor;
import org.mvel2.templates.TemplateRuntime;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> a = new HashMap<String, String>();
		a.put("b", "c");
		map.put("a", a);
//		Object obj = MVEL.evalToString("I'm @{a}", map);
//		System.out.println(obj);
		
		System.out.println(TemplateRuntime.eval("I'm @{a.b}", map));
		
	}

}
