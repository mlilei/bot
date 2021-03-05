package com.mlilei.bot;

import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;
import com.deep007.goniub.selenium.mitm.GoniubChromeOptions;
import com.google.common.base.Splitter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        EXECUTOR_SERVICE.submit(this::rateSubmit);
        System.out.println("BotGoStarter.init over");
    }

    @Autowired
    private Map<String, Operation> operationMap;

    @SneakyThrows
    public void rateSubmit() {
        LOGGER.info("rateStarter start");

        while (true) {

            LOGGER.info("a task submit");
            EXECUTOR_SERVICE.submit(this::starter);


            try {
                final long rate = Long.parseLong(PROPERTIES.getProperty("rate"));
                long sleep = TimeUnit.MINUTES.toMillis(1) / rate;
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void starter() {

        final GoniubChromeOptions chromeOptions = new GoniubChromeOptions(
                false,
                false,
                false,
                null,
                UaHelper.getUa()
        );
        LOGGER.info("ua={}", chromeOptions.getUserAgent());
        final GoniubChromeDriver driver = new GoniubChromeDriver(chromeOptions);

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
            }

        } finally {
            driver.quit();
        }
    }
}
