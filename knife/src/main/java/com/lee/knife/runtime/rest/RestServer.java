package com.lee.knife.runtime.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.lee.knife.runtime.rest.resources.LogResource;
import com.lee.knife.runtime.rest.resources.RootResource;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Created on 2016/1/27.
 */
public class RestServer {
  private static final Logger log = Logger.getLogger(RestServer.class);
  private static final long GRACEFUL_SHUTDOWN_TIMEOUT_MS = 60 * 1000;

  private static final ObjectMapper JSON_SERDE = new ObjectMapper();

  private Server jettyServer;
  private ResourceConfig resourceConfig;

  private String hostname = "0.0.0.0";
  private int port = 12306;

  /**
   * Create a REST server for this herder using the specified configs.
   */
  public RestServer(String hostname, int port) {
    this.hostname = hostname;
    this.port = port;
    this.initServer();
  }

  public RestServer() {
    this.initServer();
  }

  private void initServer() {
    jettyServer = new Server();

    ServerConnector connector = new ServerConnector(jettyServer);
    if (hostname != null && !hostname.isEmpty())
      connector.setHost(hostname);
    connector.setPort(port);

    if (log.isInfoEnabled())
      log.info("config rest server with host:" + hostname + " ,port:" + port);

    jettyServer.setConnectors(new Connector[] { connector });
    //配置默认的resource，如可以更改的
    this.resourceConfig = new ResourceConfig();
    this.resourceConfig.register(new JacksonJsonProvider());

    this.resourceConfig.register(RootResource.class);
    this.resourceConfig.register(LogResource.class);

  }

  public void start() throws Exception {
    log.info("Starting REST server .....");

    ServletContainer servletContainer = new ServletContainer(resourceConfig);
    ServletHolder servletHolder = new ServletHolder(servletContainer);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    context.addServlet(servletHolder, "/*");

    RequestLogHandler requestLogHandler = new RequestLogHandler();
    Slf4jRequestLog requestLog = new Slf4jRequestLog();
    requestLog.setLoggerName(RestServer.class.getCanonicalName());
    requestLog.setLogLatency(true);
    requestLogHandler.setRequestLog(requestLog);

    HandlerCollection handlers = new HandlerCollection();
    handlers.setHandlers(new Handler[] { context, new DefaultHandler(), requestLogHandler });

        /* Needed for graceful shutdown as per `setStopTimeout` documentation */
    StatisticsHandler statsHandler = new StatisticsHandler();
    statsHandler.setHandler(handlers);
    jettyServer.setHandler(statsHandler);
    jettyServer.setStopTimeout(GRACEFUL_SHUTDOWN_TIMEOUT_MS);
    jettyServer.setStopAtShutdown(true);

    jettyServer.start();

  }

  public void stop() throws Exception {
    try {
      jettyServer.stop();
      jettyServer.join();
    } finally {
      jettyServer.destroy();
    }
  }

  /**
   * 将resourceConfig对外开放，用户可以自定义自己的resource
   * 并利用已经搭建的restful，完成对外的接口提供
   * resource编写可以参考 <code>LogResource</code>
   */
  public ResourceConfig getResourceConfig() {
    return this.resourceConfig;
  }

  /**
   * for test start
   *
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    RestServer server = new RestServer();
    server.start();
    new LOGTest().run();
  }
}
