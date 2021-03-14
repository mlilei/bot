package com.mlilei.bot.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @Author lilei
 * @Description
 * @Date 2021/2/24 16:16
 */
public class ConfigHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHelper.class);

    private static Properties PROPERTIES = new Properties();

    static {
        try {
            File file = new File("D:\\bot\\conf.properties");
            if (!file.exists()) {
                LOGGER.error("配置加载失败，文件不存在 conf.properties");
                throw new RuntimeException("配置加载失败，文件不存在 D:\\bot\\conf.properties");
            }
            try (InputStream inputStream = new FileInputStream(file);
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                Properties properties = new Properties();
                properties.load(inputStreamReader);
                PROPERTIES = properties;
                LOGGER.info("配置加载完成 {}", properties);
            }

        } catch (Exception e) {
            LOGGER.error("配置加载失败", e);
        }
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }
}
