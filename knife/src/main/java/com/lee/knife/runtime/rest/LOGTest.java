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
}
