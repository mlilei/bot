package com.mlilei.bot;

import com.deep007.goniub.request.HttpsProxy;
import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;
import com.deep007.goniub.selenium.mitm.GoniubChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author lilei
 * @Description
 * @Date 2021/3/2 17:43
 */
@Component
public class BotGoStarter {

    @Autowired
    private Map<String, Operation> operationMap;


    public void starter() {


        final GoniubChromeOptions chromeOptions = new GoniubChromeOptions(
                false,
                false,
                false,
                null,
                null
        );


        final GoniubChromeDriver driver = new GoniubChromeDriver(chromeOptions);

        String step = ConfigHelper.PROPERTIES.getProperty("step");

        String process = ConfigHelper.PROPERTIES.getProperty("process");
        final Operation operation = operationMap.get(process);
        for (String op : step.split("->")) {

            switch (op) {
                case "search":
                    operation.search(driver);
                    break;
                case "nextPage":
                    operation.nextPage(driver);
                    break;
                case "randomVisit":
                    operation.randomVisit(driver);
                    break;
                case "back":
                    operation.back(driver);
                    break;
                case "forward":
                    operation.forward(driver);
                    break;
                case "downward":
                    operation.downward(driver);
                    break;
                case "bottom":
                    operation.bottom(driver);
                    break;
                default:
            }
        }

        driver.quit();
    }
}
