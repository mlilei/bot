package com.mlilei.bot;

import com.alibaba.fastjson.JSON;
import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;
import com.deep007.goniub.selenium.mitm.GoniubChromeOptions;
import com.google.common.base.Splitter;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static com.mlilei.bot.BotApplication.EXECUTOR_SERVICE;
import static com.mlilei.bot.ConfigHelper.PROPERTIES;

/**
 * @Author lilei
 * @Description
 * @Date 2021/3/2 17:43
 */
@Component
public class BotGoStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BotGoStarter.class);

    private static final Splitter.MapSplitter SPLITTER = Splitter.on(",").trimResults().withKeyValueSeparator("=");

    private static final String STEP_SPLITTER = "->";

    @PostConstruct
    public void init() {
        EXECUTOR_SERVICE.submit(this::scheduleStarter);
    }

    @Autowired
    private Map<String, Operation> operationMap;


    public void scheduleStarter() {
        try {
            LOGGER.info("execute scheduleStarter");
            final double rate = Double.parseDouble(PROPERTIES.getProperty("rate"));
            LOGGER.info("rate={}", rate);
            final RateLimiter rateLimiter = RateLimiter.create(rate / 60);
            while (true) {
                rateLimiter.acquire();
                EXECUTOR_SERVICE.submit(new Runnable() {
                    @Override
                    public void run() {
                        Thread.currentThread().setName(UUID.randomUUID().toString());
                        starter();
                    }
                });
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    public void starter() {
        LOGGER.info("execute starter");
        final GoniubChromeOptions chromeOptions = new GoniubChromeOptions(
                Boolean.parseBoolean(PROPERTIES.getProperty("disableLoadImage", "true")),
                Boolean.parseBoolean(PROPERTIES.getProperty("headless", "true")),
                Boolean.parseBoolean(PROPERTIES.getProperty("hideFingerprint", "false")),
                ProxyHelper.getProxy(),
                UaHelper.getUa()
        );
        LOGGER.info("chromeOptions={}", JSON.toJSONString(chromeOptions));

        final GoniubChromeDriver driver = new GoniubChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0L));
        try {
            String process = PROPERTIES.getProperty("process");
            final Operation operation = operationMap.get(process);
            String step = PROPERTIES.getProperty("step");
            for (String node : step.split(STEP_SPLITTER)) {

                final String[] split = node.split("\\|");
                String op = split[0].trim();
                Map<String, String> params = Collections.emptyMap();
                if (split.length > 1) {
                    params = SPLITTER.split(split[1]);
                }
                LOGGER.info("op={}, params={}", op, params);
                try {
                    switch (op) {
                        case "search":
                            operation.search(driver, params);
                            break;
                        case "nextPage":
                            operation.nextPage(driver, params);
                            break;
                        case "randomVisit":
                            operation.randomVisit(driver, params);
                            break;
                        case "back":
                            operation.back(driver, params);
                            break;
                        case "forward":
                            operation.forward(driver, params);
                            break;
                        case "downward":
                            operation.downward(driver, params);
                            break;
                        case "bottom":
                            operation.bottom(driver, params);
                            break;
                        case "delay":
                            operation.delay(driver, params);
                        default:
                    }


                    if (!operation.checkPage(driver)) {
                        LOGGER.info("页面校验失败");
                        break;
                    }

                } catch (Exception e) {
                    LOGGER.error("", e);
                }
            }

        } finally {
            driver.quit();
        }
    }
}
