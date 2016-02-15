package com.lee.knife.runtime.rest;

import org.apache.log4j.Logger;

/**
 * Created on 2016/1/29.
 */
public class LOGTest {
  private static Logger LOG = Logger.getLogger(LOGTest.class);
  public void run()throws Exception{
    while (true){
      LOG.debug("hello debug");
      LOG.info("hello info");
      Thread.sleep(3000);
    }
  }
  /**
   * for test
   * 通过restful的方式来修改日志级别(rest uri见: {@codeLogResource}
   * e.g:
   * http://xxxx:12306/logLevel/set?logName=com.lee.knife.runtime.rest.LOGTest&level=DEBUG
   */
  public static void main(String[] args) throws Exception {
    RestServer server = new RestServer();
    server.start();
    new LOGTest().run();
  }
}
