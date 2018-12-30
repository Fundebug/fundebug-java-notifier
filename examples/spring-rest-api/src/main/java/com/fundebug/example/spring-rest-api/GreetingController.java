package com.fundebug.example.spring-rest-api

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.annotation.ExceptionHandler;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(method=RequestMethod.GET)
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
//    	throw new NullPointerException("TEST");
//  	  String TEST03=null;
//      boolean eq=TEST03.equals(""); 
    		  return new Greeting(counter.incrementAndGet(),
                      String.format(template, name));
    }
 
    // 无法捕获HttpRequestMethodNotSupportedException
//  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
//  public void request405(HttpRequestMethodNotSupportedException ex) {
//	  ex.printStackTrace();
//  }
}
