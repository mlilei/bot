package com.mlilei.bot;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                init();
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(timerTask, 1, TimeUnit.MINUTES);
    }


    public static void init() {

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

    @SneakyThrows
    public static void load() {
        while (true) {
            Thread.sleep(60000);
            init();
        }
    }
}
