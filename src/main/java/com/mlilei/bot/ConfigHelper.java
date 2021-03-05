package com.mlilei.bot;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import static com.mlilei.bot.BotApplication.EXECUTOR_SERVICE;

/**
 * @Author lilei
 * @Description
 * @Date 2021/2/24 16:16
 */

public class ConfigHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHelper.class);

    public static Properties PROPERTIES = new Properties();

    static {
        init();
        EXECUTOR_SERVICE.submit(ConfigHelper::load);
    }


    public static void init() {
        try {
            InputStream inputStream = ConfigHelper.class.getClassLoader().getResourceAsStream("conf.properties");
            if (null == inputStream) {
                LOGGER.error("配置加载失败，文件不存在 conf.properties");
            } else {
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                Properties properties = new Properties();
                properties.load(inputStreamReader);
                PROPERTIES = properties;
                LOGGER.info("配置加载完成 {}", properties);
            }
        } catch (Exception e) {
            LOGGER.error("配置加载失败", e);
        }
    }

    @SneakyThrows
    public static void load() {
        while (true) {
            Thread.sleep(10000);
            init();
        }
    }


}
