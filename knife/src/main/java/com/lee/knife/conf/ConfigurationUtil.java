package com.lee.knife.conf;

import com.google.common.collect.Maps;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.Map;

/**
 * 负责加载properties配置文件
 */
public class ConfigurationUtil {

  private static final Logger LOG = Logger.getLogger(ConfigurationUtil.class);

  private static Map<String, Configuration> confMaps = Maps.newHashMap();

  /**
   * 加载classpath下，指定的property配置文件内容。返回值有可能为null，需要额外应用端处理
   * @param propertiesName 属性文件名称，这里是指properties的配置文件
   * @return 返回commons-configuration中包装的Configuration实例。
   */
  public static Configuration getConfiguration(String propertiesName) {

    if (StringUtils.isBlank(propertiesName)) {
      LOG.error("You did not specify the properties name.");
      return null;
    }

    if (!propertiesName.endsWith("properties")) {
      LOG.error("Just supporting properties files.Your file is invalid suffix "+propertiesName);
      return null;
    }

    Configuration conf = null;
    URL url = ConfigurationUtil.class.getClassLoader().getResource(propertiesName);
    synchronized (confMaps) {
      conf = confMaps.get(propertiesName);
      try {
        if (conf == null) {
          conf = new PropertiesConfiguration(url);
          LOG.info("Loading properties file "+url.getPath());
          confMaps.put(propertiesName, conf);
        }
      } catch (ConfigurationException e) {
        LOG.error("Can not initialize property file "+propertiesName,e);
      }
    }
    return conf;
  }

}
