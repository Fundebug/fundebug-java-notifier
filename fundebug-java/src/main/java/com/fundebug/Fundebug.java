package com.fundebug;

import java.util.Map;
import java.util.List;

public class Fundebug {
	private Event event;
	
	public Fundebug(String apiKey){
		if(apiKey == null){
			throw new NullPointerException("请设置apiKey");
		}
		
		// initialize event
		this.event = new Event(apiKey);
		
		// enable catching uncaught error
		ExceptionHandler.enableFundebugHandler(this.event);
	}

	public void notifyError(Throwable e){
		this.event.notifyError(e);
	}
	
	public void notify(String name, String message) {
		this.event.notify(name, message);
	}
	
	public void setMetaData(Map<String, Object> metaData) {
		event.setMetaData(metaData);
	}
	
	public void setReleaseStage(String releaseStage) {
		event.setReleaseStage(releaseStage);
	}
	
	public void setSilent(Boolean silent) {
		event.setSilent(silent);
	}
	
	public void setAppVersion(String appVersion) {
		event.setAppVersion(appVersion);
	}
	
	public void setFilters(List<Map<String, String>> filters) {
		event.setFilters(filters);
	}
	
	public void disable(){
		ExceptionHandler.disableFundebugHandler();
	}
}
