package com.mlilei.bot;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
//        final String path = BotStarter.class.getClassLoader().getResource("").getPath();
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\chromedriver.exe");
    }


    public void starter() {


        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");

//        chromeOptions.addArguments("--headless");

        WebDriver driver = new ChromeDriver(chromeOptions);

//        ((JavascriptExecutor)driver).executeScript("Page.addScriptToEvaluateOnNewDocumen",Files.r)


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


    public static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ignored) {
        }
    }


}
