package com.mlilei.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @Author lilei
 * @Description
 * @Date 2021/2/24 16:16
 */
@Component
public class ConfigHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHelper.class);

    public static Properties PROPERTIES = new Properties();

    static {
        try {
            File file = new File("D:\\conf.properties");
            if (!file.exists()) {
                file = new File("C:\\conf.properties");
                if (!file.exists()) {
                    LOGGER.error("配置加载失败，文件不存在 conf.properties");
                    throw new RuntimeException("配置加载失败，文件不存在 D:\\conf.properties");
                }
            }
            try (InputStream inputStream = new FileInputStream(file);
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
                Properties properties = new Properties();
                properties.load(inputStreamReader);
                PROPERTIES = properties;
                LOGGER.info("配置加载完成 {}", properties);
            }

        } catch (Exception e) {
            LOGGER.error("配置加载失败", e);
        }
    }
}
