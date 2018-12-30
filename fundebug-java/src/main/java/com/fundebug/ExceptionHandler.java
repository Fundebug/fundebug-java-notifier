package com.fundebug;

import java.lang.Thread.UncaughtExceptionHandler;

class ExceptionHandler implements UncaughtExceptionHandler {
	private final UncaughtExceptionHandler oldHandler;
	private static Event event = null;

    ExceptionHandler(UncaughtExceptionHandler oldHandler) {
        this.oldHandler = oldHandler;
    }
    
    
    static void enableFundebugHandler(Event e){

    	UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
    	// update default uncaughtExceptino Handler with ours.
    	Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(handler));
    	
    	event = e;
    }
    
    // For testing
    static void disableFundebugHandler(){
    	UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
        if (handler instanceof ExceptionHandler) {
            ExceptionHandler ehandler = (ExceptionHandler) handler;
            Thread.setDefaultUncaughtExceptionHandler(ehandler.oldHandler);
        }
    }
    
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		// notify to our server
		event.notifyError(t, e);
		// call the old one: 不知道为什么 oldHandler 是 null !!! TODO
		 // System.out.println(this.oldHandler);
		if (this.oldHandler != null) {
			this.oldHandler.uncaughtException(t, e);
        } else {
            System.err.printf("Exception in thread \"%s\" ", t.getName());
            e.printStackTrace(System.err);
        }
	}
}
