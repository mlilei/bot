package com.mlilei.bot;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author lilei
 * @Description
 * @Date 2021/2/23 11:59
 */
@Component
public class BotStarter {

    @Autowired
    private Map<String, Operation> operationMap;

    static {
        final String path = BotStarter.class.getClassLoader().getResource("").getPath();
        System.setProperty("webdriver.chrome.driver", path + "\\chromedriver.exe");
    }


    public void starter() {

        String process = ConfigHelper.PROPERTIES.getProperty("process");
        final Operation operation = operationMap.get(process);

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver,10);


        String step = ConfigHelper.PROPERTIES.getProperty("step");

        for (String op : step.split("->")) {

            switch (op){
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


    public static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ignored) {
        }
    }


}
