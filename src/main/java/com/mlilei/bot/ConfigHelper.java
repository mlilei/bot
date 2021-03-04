package com.mlilei.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * @Author lilei
 * @Description
 * @Date 2021/2/24 16:16
 */

public class ConfigHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHelper.class);

    public static final Properties PROPERTIES = new Properties();

    static {
        try {
            InputStream inputStream = ConfigHelper.class.getClassLoader().getResourceAsStream("conf.properties");
            if (null == inputStream) {
                LOGGER.error("配置加载失败，文件不存在 conf.properties");
            } else {
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                PROPERTIES.load(inputStreamReader);
                System.out.println(PROPERTIES);
                LOGGER.info("配置加载完成");
            }
        } catch (Exception e) {
            LOGGER.error("配置加载失败", e);
        }
    }
}
