package com.mlilei.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
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
        final String path = BotStarter.class.getClassLoader().getResource("").getPath();
        try {
            InputStream inputStream = new FileInputStream(path + "\\config.txt");
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            PROPERTIES.load(inputStreamReader);
            System.out.println(PROPERTIES);
            LOGGER.info("读取配置完成");
        } catch (Exception e) {
            LOGGER.error("读取配置异常", e);
        }
    }
}
