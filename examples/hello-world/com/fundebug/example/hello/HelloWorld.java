package com.fundebug.example.hello;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import com.fundebug.Fundebug;

public class HelloWorld {

	private static Fundebug fundebug;

	public static void main(String[] args) {
		fundebug = new Fundebug("716b45610ebaee70faa193663a0aa03813d8fcbb5c1ee9201c0417e17ed5a728");
		configFundebug();
		testFundebug();

	}

	// 配置Fundebug
	private static void configFundebug() {

		// 配置appVersion
		fundebug.setAppVersion("1.0.0");

		// 配置releaseStage
		fundebug.setReleaseStage("development");

		// 配置silent
//		fundebug.setSilent(true);

		// 配置metaData
		Map<String, Object> metaData = new HashMap<String, Object>();
		metaData.put("Company", "Fundebug");
		metaData.put("CEO", "Stefan");
		fundebug.setMetaData(metaData);

		// 配置过滤条件
		List<Map<String, String>> filters = new ArrayList<Map<String, String>>();
		Map<String, String> filter1 = new HashMap<String, String>();
		filter1.put("class", "java.lang.NullPointerException");
		filter1.put("hostname", "lijingdeMacBook-Pro.local");
		Map<String, String> filter2 = new HashMap<String, String>();
		filter2.put("class", "java.lang.NullPointerException");
		filters.add(filter1);
		filters.add(filter2);
		fundebug.setFilters(filters);
	}

	// 测试各种错误
	private static void testFundebug() {
		// 测试fundebug.notify
//		fundebug.notify("TEST 01", "Hello Fundebug!");

		// 测试fundebug.notifyError
//		fundebug.notifyError(new RuntimeException("TEST 02"));

		// 测试try...catch
//		try {
//			  String TEST03=null;
//		      boolean eq=TEST03.equals(""); 	
//		}
//		catch(Exception e)
//		{
//			fundebug.notifyError(e);
//		}

		// 测试throw
//		throw new NullPointerException("TEST 04");
//		throw new ClassCastException("TEST 05");

		// 测试ServletException
		try {
			throw new ServletException("TEST 06");
		} catch (ServletException e) {
			fundebug.notifyError(e);
		}
	}
}
