package com.fundebug;

//import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Date;
import java.util.List;

public class Event implements JsonSerializable {
	
	// notifier information
	private static final String notifierVersion = "0.0.1";
//	private static final String apiUrl = "http://192.168.59.2:10014/event";
//	private static final String apiUrl = "http://localhost:10014/event";
	private static final String apiUrl = "https://java.fundebug.net/event/";
	
	// basic APP setting
	private String apiKey;
	private String appVersion;
	private String releaseStage;
	private Map<String, Object> metaData;
	private boolean silent;
	private List<Map<String, String>> filters;
	private String type;
	
	// Device information
	private String hostname;
	private String osName;
	private String osVersion;
	private String osArch;
	private String runtimeName;
	private String runtimeVersion;
	private String locale;
	
	// error information
	private String threadName;
	private String stacktrace;
	private String name;
	private String message;
	
	
	// constructor
	public Event(){
		this.initialize();
	}
		
	public Event(String apiKey) {
		this.initialize();
		this.apiKey = apiKey;
	}

	private void initialize(){
		this.hostname = getHostname();
		this.osName = System.getProperty("os.name");
		this.osVersion = System.getProperty("os.version");
		this.osArch = System.getProperty("os.arch");
		this.runtimeName = System.getProperty("java.runtime.name");
		this.runtimeVersion = System.getProperty("java.runtime.version");
		this.locale = Locale.getDefault().toString();
	}
	
	public void setApiKey(String apiKey){
		this.apiKey = apiKey;
	}
	
	public void setAppVersion(String appVersion){
		this.appVersion = appVersion;
	}
	
	public void setReleaseStage(String releaseStage){
		this.releaseStage = releaseStage;
	}

	// TODO: revised later
    private String getHostname() {
        // windows
        if (System.getProperty("os.name").startsWith("Windows")) {
            return System.getenv("COMPUTERNAME");
        }

        // Linux system
        String hostname = System.getenv("HOSTNAME");
        if (hostname != null) {
            return hostname;
        }

        // Resort to dns hostname lookup
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            // Give up
        	ex.printStackTrace();
        }

        return null;
    }

    
    public void notifyError(Throwable e)  {
    	this.setStrackTrace(e);
		try {
			this.sendToFundebug();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void notifyError(Thread t, Throwable e) {
		this.type = "uncaught";
		this.setStrackTrace(e);		
		this.setThreadName(t);
		try {
			this.sendToFundebug();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void notify(String name, String message) {
		this.name = name;
		this.message = message;
		this.type = "notification";
		try {
			this.sendToFundebug();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	public void print(){
		System.out.println("apiUrl: " + Event.apiUrl);
		System.out.println("notifier version: " + Event.notifierVersion);
		System.out.println("apiKey: " + this.apiKey);
		System.out.println("appVersion: " + this.appVersion);
		System.out.println("releaseStage: " + this.releaseStage);
		System.out.println("hostname: " + this.hostname);
		System.out.println("osName: "+ this.osName);
		System.out.println("osVersion: " + this.osVersion);
		System.out.println("osArch: " + this.osArch);
		System.out.println("runtimeName: " + this.runtimeName);
		System.out.println("runtimeVersion" + this.runtimeVersion);
		System.out.println("locale: " + this.locale);
		System.out.println("thread name: " + this.threadName);
		System.out.println("stacktrace: " + this.stacktrace);
	}
	
	public void setThreadName(Thread t){
		this.threadName = t.getName();
	}
	
	public void setStrackTrace(Throwable e){
		this.stacktrace = this.stackTraceToString(e);
	}
	
	public void setMetaData(Map<String, Object> metaData) {
		this.metaData = metaData;
	}
	
	public void setSilent(Boolean silent) {
		this.silent = silent;
	}
	
	public void setFilters(List<Map<String, String>> filters) {
		this.filters = filters;
	}
	
	private String stackTraceToString(Throwable e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}


	public void sendToFundebug() throws IOException{
		
		if(this.silent == true) return;

		if(this.filters != null && this.type != "notification")
		{
			boolean[] boo = new boolean[this.filters.size()]; 
			for(int i = 0; i < this.filters.size(); i++) {
				boo[i] = true;
				for(Map.Entry<String, String> entry : this.filters.get(i).entrySet())
				{
					Pattern pattern = Pattern.compile(entry.getValue());
					String key = getFilterCondition(entry.getKey());
					if(key == null) {
						System.out.println("不能配置key为" + entry.getKey() + "的过滤条件");
						return;
					}
					Matcher matcher = pattern.matcher(key);
					if(matcher.lookingAt() == false) boo[i] = false;
				}

				if(boo[i] == true) {
					return;
				}
			}
		}
		
		Sender sender = new Sender(Event.apiUrl);
		sender.send(this);
	}
	
	private String getFilterCondition(String filterKey) {
		String result = null;
		if(filterKey == "class") result = this.stacktrace;
		if(filterKey == "hostname") result = this.hostname;
		if(filterKey == "osName") result = this.osName;
		if(filterKey == "osVersion") result = this.osVersion;
		if(filterKey == "osArch") result = this.osArch;
		if(filterKey == "runtimeName") result = this.runtimeName;
		if(filterKey == "runtimeVersion") result = this.runtimeVersion;
		if(filterKey == "locale") result = this.locale;
		if(filterKey == "threadName") result = this.threadName;
		
		return result;
	}

	public String toJson() {
		return new Serializer(true).serialize(this);
	}

	@Override
	public Map<String, Object> asJson() {
        Map<String, Object> obj = new LinkedHashMap<String, Object>();
        if(Event.notifierVersion != null){obj.put("notifierVersion", Event.notifierVersion);}
     
        obj.put("apiKey", this.apiKey);
        if(this.appVersion != null){obj.put("appVersion", this.appVersion);}
        if(this.releaseStage != null){obj.put("releaseStage", this.releaseStage);}
        obj.put("createTime", new Date());
        if(this.metaData != null) {obj.put("metaData", this.metaData);}
        if(this.filters != null) {obj.put("filters", this.filters);}
        
        if(this.hostname != null){obj.put("hostname", this.hostname);}
        if(this.osName != null){obj.put("osName", this.osName);}
        if(this.osVersion != null){obj.put("osVersion", this.osVersion);}
        if(this.osArch != null){obj.put("osArch", this.osArch);}
        if(this.runtimeName!= null){obj.put("runtimeName", this.runtimeName);}
        if(this.runtimeVersion != null){obj.put("runtimeVersion", this.runtimeVersion);}
        if(this.locale != null){obj.put("locale", this.locale);}
    	
    	// error information
        if(this.threadName != null){obj.put("threadName", this.threadName);}
        if(this.stacktrace != null){obj.put("stacktrace", this.stacktrace);}
        if(this.name != null) {obj.put("name", this.name);}
        if(this.message != null) {obj.put("message", this.message);}
    	if(this.type != null) {obj.put("type", this.type);}
        
        return obj;
    }
}
