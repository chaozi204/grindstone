package com.lee.knife.runtime.rest.resources;

import com.lee.knife.cons.ExitCode;
import com.lee.knife.runtime.rest.entities.LogOperationMessage;
import com.lee.knife.runtime.rest.entities.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Jdk14Logger;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;

@Path("/logLevel")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LogResource {

  private static final Logger LOG = Logger.getLogger(LogResource.class);

  @javax.ws.rs.core.Context
  private ServletContext context;

  /**
   * 处理uri为 /logLevel 请求
   *
   * @return
   */
  @GET
  public Message defaultQuest() {
    return new Message(ExitCode.CONSULT_CODE.getCode(), "Using URI with /logLevel/get or /logLevel/set");
  }

  /**
   * 处理uri为 /logLevel/set 请求
   *
   * @return
   */
  @GET
  @Path("/set")
  public LogOperationMessage setLogLevel(@QueryParam("logName") String logName, @QueryParam("level") String level) {
    LOG.info("set Logger information for "+logName+" with logger level "+level);

    if (StringUtils.isBlank(level) || StringUtils.isBlank(logName)) {
      return new LogOperationMessage("set", "logName or level can not empty", level, "fail");
    }

    Log log = LogFactory.getLog(logName);
    String operation = "set";
    LogOperationMessage operationMessage = null;
    String message = operation + " " + logName + " set level is " + level.toString();

    if (log instanceof Log4JLogger) {
      org.apache.log4j.Logger log1 = ((Log4JLogger) log).getLogger();
      if (!level.equals(org.apache.log4j.Level.toLevel(level).toString())) {
        operationMessage = new LogOperationMessage(operation, message, level, "fail");
      } else {
        log1.setLevel(org.apache.log4j.Level.toLevel(level));
        operationMessage = new LogOperationMessage(operation, message, level, "success");
      }
    } else if (log instanceof Jdk14Logger) {
      java.util.logging.Logger log1 = ((Jdk14Logger) log).getLogger();
      log1.setLevel(java.util.logging.Level.parse(level));
      operationMessage = new LogOperationMessage(operation, message, level, "success");
    } else {
      operationMessage = new LogOperationMessage(operation,
          "only Log4JLogger and Jdk14Logger support set level operation", level, "fail");
    }

    return operationMessage;
  }

  /**
   * 处理uri为 /logLevel/get 请求
   * @return
   */
  @GET
  @Path("/get")
  public LogOperationMessage getLogLevel(@QueryParam("logName") String logName) {

    LOG.info("Get Logger information for "+logName);

    Log log = LogFactory.getLog(logName);
    String operation = "get";
    LogOperationMessage operationMessage = null;

    if (log instanceof Log4JLogger) {
      org.apache.log4j.Logger log1 = ((Log4JLogger) log).getLogger();
      String level = log1.getEffectiveLevel().toString();
      String message = operation + " Log4JLogger:" + logName + " level is " + level;
      operationMessage = new LogOperationMessage(operation, message, level, "success");
    } else if (log instanceof Jdk14Logger) {
      java.util.logging.Logger log1 = ((Jdk14Logger) log).getLogger();
      String level =log1.getLevel().toString();
      String message = operation + " Jdk14Logger:" + logName + " level is " + level;
      operationMessage = new LogOperationMessage(operation, message, level, "success");
    } else {
      operationMessage = new LogOperationMessage(operation, "can not find logname", "none", "fail");
    }

    return operationMessage;
  }

}
